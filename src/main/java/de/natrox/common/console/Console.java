package de.natrox.common.console;

import de.natrox.common.console.handler.InputHandler;
import de.natrox.common.console.handler.CompleteHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public interface Console extends AutoCloseable {

    static Builder builder() {
        return new JLine3ConsoleBuilder();
    }

    void togglePrinting(boolean enabled);

    boolean printingEnabled();

    default boolean hasAnimationSupport() {
        return this.hasColorSupport();
    }

    @NotNull @Unmodifiable Collection<String> commandHistory();

    void commandHistory(@Nullable Collection<String> history);

    void commandInputValue(@NotNull String commandInputValue);

    @NotNull CompletableFuture<String> readLine();

    void enableAllHandlers();

    void disableAllHandlers();

    void enableAllCommandHandlers();

    void disableAllCommandHandlers();

    void enableAllTabCompleteHandlers();

    void disableAllTabCompleteHandlers();

    void addCommandHandler(@NotNull UUID uniqueId, @NotNull InputHandler handler);

    void removeCommandHandler(@NotNull UUID uniqueId);

    void addTabCompleteHandler(@NotNull UUID uniqueId, @NotNull CompleteHandler handler);

    void removeTabCompleteHandler(@NotNull UUID uniqueId);

    @NotNull Console writeRaw(@NotNull Supplier<String> rawText);

    @NotNull Console forceWriteLine(@NotNull String text);

    @NotNull Console writeLine(@NotNull String text);

    boolean hasColorSupport();

    boolean usingMatchingHistoryComplete();

    void usingMatchingHistoryComplete(boolean matchingHistoryComplete);

    @NotNull String prompt();

    void prompt(@NotNull String prompt);

    void resetPrompt();

    void removePrompt();

    void emptyPrompt();

    void clearScreen();

    int width();

    int displayLength(@NotNull String string);

    interface Builder {

        @NotNull Builder prompt(@NotNull Supplier<String> prompt);

        @NotNull Console build() throws Exception;

    }
}
