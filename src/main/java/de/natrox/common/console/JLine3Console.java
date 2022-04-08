package de.natrox.common.console;

import de.natrox.common.console.color.ConsoleColor;
import de.natrox.common.console.handler.CompleteHandler;
import de.natrox.common.console.handler.InputHandler;
import de.natrox.common.console.handler.Toggleable;
import de.natrox.common.logger.LogManager;
import de.natrox.common.logger.Logger;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jline.reader.LineReader;
import org.jline.reader.LineReader.Option;
import org.jline.reader.LineReader.SuggestionType;
import org.jline.reader.impl.LineReaderImpl;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;
import org.jline.utils.WCWidth;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

public final class JLine3Console implements Console {

    private static final Logger LOGGER = LogManager.logger(JLine3Console.class);

    private final Lock printLock = new ReentrantLock(true);

    private final Map<UUID, InputHandler> consoleInputHandler = new ConcurrentHashMap<>();
    private final Map<UUID, CompleteHandler> tabCompleteHandler = new ConcurrentHashMap<>();

    private final ConsoleReadThread consoleReadThread = new ConsoleReadThread(this);
    private final ExecutorService animationThreadPool = Executors.newCachedThreadPool();

    private final Terminal terminal;
    private final LineReaderImpl lineReader;

    private final Supplier<String> defaultPrompt;
    private String prompt;

    private boolean printingEnabled = true;
    private boolean matchingHistorySearch = true;

    protected JLine3Console(Supplier<String> defaultPrompt) throws Exception {
        try {
            AnsiConsole.systemInstall();
        } catch (Throwable ignored) {
        }

        this.terminal = TerminalBuilder
            .builder()
            .system(true)
            .encoding(StandardCharsets.UTF_8)
            .build();
        this.lineReader = new InternalLineReader(this.terminal);

        this.lineReader.setAutosuggestion(SuggestionType.COMPLETER);
        this.lineReader.setCompleter(new JLine3Completer(this));

        this.lineReader.option(Option.AUTO_GROUP, false);
        this.lineReader.option(Option.AUTO_MENU_LIST, true);
        this.lineReader.option(Option.AUTO_FRESH_LINE, true);
        this.lineReader.option(Option.EMPTY_WORD_OPTIONS, false);
        this.lineReader.option(Option.HISTORY_TIMESTAMPED, false);
        this.lineReader.option(Option.DISABLE_EVENT_EXPANSION, true);

        this.lineReader.variable(LineReader.BELL_STYLE, "none");
        this.lineReader.variable(LineReader.HISTORY_SIZE, 500);
        this.lineReader.variable(LineReader.HISTORY_FILE_SIZE, 2500);
        this.lineReader.variable(LineReader.COMPLETION_STYLE_LIST_BACKGROUND, "inverse");

        this.defaultPrompt = defaultPrompt;

        this.resetPrompt();
        this.updatePrompt();
        this.consoleReadThread.start();
    }

    @Override
    public void togglePrinting(boolean enabled) {
        this.printingEnabled = enabled;
    }

    @Override
    public boolean printingEnabled() {
        return this.printingEnabled;
    }

    @Override
    public @NotNull Collection<String> commandHistory() {
        var result = new ArrayList<String>();
        for (var entry : this.lineReader.getHistory()) {
            result.add(entry.line());
        }

        return result;
    }

    @Override
    public void commandHistory(@Nullable Collection<String> history) {
        try {
            this.lineReader.getHistory().purge();
        } catch (IOException exception) {
            LOGGER.severe("Exception while purging the console history", exception);
        }

        if (history != null) {
            for (var s : history) {
                this.lineReader.getHistory().add(s);
            }
        }
    }

    @Override
    public void commandInputValue(@NotNull String commandInputValue) {
        this.lineReader.getBuffer().write(commandInputValue);
    }

    @Override
    public @NotNull CompletableFuture<String> readLine() {
        return this.consoleReadThread.currentTask();
    }

    @Override
    public void enableAllHandlers() {
        this.enableAllCommandHandlers();
        this.enableAllTabCompleteHandlers();
    }

    @Override
    public void disableAllHandlers() {
        this.disableAllCommandHandlers();
        this.disableAllTabCompleteHandlers();
    }

    @Override
    public void enableAllCommandHandlers() {
        this.toggleHandlers(true, this.consoleInputHandler.values());
    }

    @Override
    public void disableAllCommandHandlers() {
        this.toggleHandlers(false, this.consoleInputHandler.values());
    }

    @Override
    public void enableAllTabCompleteHandlers() {
        this.toggleHandlers(true, this.tabCompleteHandler.values());
    }

    @Override
    public void disableAllTabCompleteHandlers() {
        this.toggleHandlers(false, this.tabCompleteHandler.values());
    }

    @Override
    public void addCommandHandler(@NotNull UUID uniqueId, @NotNull InputHandler handler) {
        this.consoleInputHandler.put(uniqueId, handler);
    }

    @Override
    public void removeCommandHandler(@NotNull UUID uniqueId) {
        this.consoleInputHandler.remove(uniqueId);
    }

    @Override
    public void addTabCompleteHandler(@NotNull UUID uniqueId, @NotNull CompleteHandler handler) {
        this.tabCompleteHandler.put(uniqueId, handler);
    }

    @Override
    public void removeTabCompleteHandler(@NotNull UUID uniqueId) {
        this.tabCompleteHandler.remove(uniqueId);
    }

    @Override
    public @NotNull Console writeRaw(@NotNull Supplier<String> rawText) {
        this.printLock.lock();
        try {
            this.print(ConsoleColor.toColouredString('&', rawText.get()));
            return this;
        } finally {
            this.printLock.unlock();
        }
    }

    @Override
    public @NotNull Console writeLine(@NotNull String text) {
        if (this.printingEnabled) {
            this.forceWriteLine(text);
        }

        return this;
    }

    @Override
    public @NotNull Console forceWriteLine(@NotNull String text) {
        this.printLock.lock();
        try {
            // ensure that the given text is formatted properly
            text = ConsoleColor.toColouredString('&', text);
            if (!text.endsWith(System.lineSeparator())) {
                text += System.lineSeparator();
            }

            this.print(Ansi.ansi().eraseLine(Ansi.Erase.ALL).toString() + '\r' + text + Ansi.ansi().reset().toString());
        } finally {
            this.printLock.unlock();
        }

        return this;
    }

    @Override
    public boolean hasColorSupport() {
        return true;
    }

    @Override
    public boolean usingMatchingHistoryComplete() {
        return this.matchingHistorySearch;
    }

    @Override
    public void usingMatchingHistoryComplete(boolean matchingHistoryComplete) {
        this.matchingHistorySearch = matchingHistoryComplete;
    }

    @Override
    public void removePrompt() {
        this.prompt = null;
        this.updatePrompt();
    }

    @Override
    public void emptyPrompt() {
        this.prompt = ConsoleColor.DEFAULT.toString();
        this.updatePrompt();
    }

    private void updatePrompt() {
        this.lineReader.setPrompt(this.prompt);
    }

    @Override
    public void clearScreen() {
        this.terminal.puts(InfoCmp.Capability.clear_screen);
        this.terminal.flush();
        this.redisplay();
    }

    @Override
    public void close() throws Exception {
        this.animationThreadPool.shutdownNow();
        this.consoleReadThread.interrupt();

        this.terminal.flush();
        this.terminal.close();

        AnsiConsole.systemUninstall();
    }

    @Override
    public @NotNull String prompt() {
        return this.prompt;
    }

    @Override
    public void prompt(@NotNull String prompt) {
        this.prompt = prompt;
        this.updatePrompt();
    }

    @Override
    public void resetPrompt() {
        this.prompt = defaultPrompt.get();
        this.updatePrompt();
    }

    @Override
    public int width() {
        return this.terminal.getWidth();
    }

    @Override
    public int displayLength(@NotNull String string) {
        var result = 0;
        // count for the length of each char in the string
        for (var i = 0; i < string.length(); i++) {
            result += Math.max(0, WCWidth.wcwidth(string.charAt(i)));
        }
        return result;
    }

    private void print(@NotNull String text) {
        this.lineReader.getTerminal().puts(InfoCmp.Capability.carriage_return);
        this.lineReader.getTerminal().puts(InfoCmp.Capability.clr_eol);
        this.lineReader.getTerminal().writer().print(text);
        this.lineReader.getTerminal().writer().flush();

        this.redisplay();
    }

    private void redisplay() {
        if (this.lineReader.isReading()) {
            this.lineReader.callWidget(LineReader.REDRAW_LINE);
            this.lineReader.callWidget(LineReader.REDISPLAY);
        }
    }

    private void toggleHandlers(boolean enabled, @NotNull Collection<? extends Toggleable> handlers) {
        for (var handler : handlers) {
            handler.enabled(enabled);
        }
    }

    @Internal
    @NotNull
    LineReader lineReader() {
        return this.lineReader;
    }

    @Internal
    @NotNull
    Map<UUID, InputHandler> consoleInputHandler() {
        return this.consoleInputHandler;
    }

    @Internal
    @NotNull
    Map<UUID, CompleteHandler> tabCompleteHandlers() {
        return this.tabCompleteHandler;
    }

    private final class InternalLineReader extends LineReaderImpl {

        private InternalLineReader(@NotNull Terminal terminal) {
            super(terminal, "Console", null);
        }

        @Override
        protected boolean historySearchBackward() {
            if (JLine3Console.this.usingMatchingHistoryComplete()) {
                return super.historySearchBackward();
            }

            if (this.history.previous()) {
                this.setBuffer(this.history.current());
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected boolean historySearchForward() {
            if (JLine3Console.this.usingMatchingHistoryComplete()) {
                return super.historySearchForward();
            }

            if (this.history.next()) {
                this.setBuffer(this.history.current());
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected boolean upLineOrSearch() {
            return this.historySearchBackward();
        }

        @Override
        protected boolean downLineOrSearch() {
            return this.historySearchForward();
        }
    }
}
