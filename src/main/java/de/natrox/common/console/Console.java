package de.natrox.common.console;

import de.natrox.common.console.handler.ConsoleInputHandler;
import de.natrox.common.console.handler.ConsoleTabCompleteHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface Console extends AutoCloseable {

    void togglePrinting(boolean enabled);

    boolean printingEnabled();

    default boolean hasAnimationSupport() {
        return this.hasColorSupport();
    }

    @Unmodifiable
    @NotNull
    Collection<String> commandHistory();

    void commandHistory(@Nullable Collection<String> history);

    void commandInputValue(@NotNull String commandInputValue);

    @NotNull
    CompletableFuture<String> readLine();

    void enableAllHandlers();

    void disableAllHandlers();

    void enableAllCommandHandlers();

    void disableAllCommandHandlers();

    void enableAllTabCompleteHandlers();

    void disableAllTabCompleteHandlers();

    void addCommandHandler(@NotNull UUID uniqueId, @NotNull ConsoleInputHandler handler);

    void removeCommandHandler(@NotNull UUID uniqueId);

    void addTabCompleteHandler(@NotNull UUID uniqueId, @NotNull ConsoleTabCompleteHandler handler);

    void removeTabCompleteHandler(@NotNull UUID uniqueId);

    @NotNull
    Console writeRaw(@NotNull String rawText);

    @NotNull
    Console forceWrite(@NotNull String text);

    @NotNull
    Console forceWriteLine(@NotNull String text);

    @NotNull
    Console write(@NotNull String text);

    @NotNull
    Console writeLine(@NotNull String text);

    boolean hasColorSupport();

    boolean usingMatchingHistoryComplete();

    void usingMatchingHistoryComplete(boolean matchingHistoryComplete);

    @NotNull
    String prompt();

    void prompt(@NotNull String prompt);

    void removePrompt();

    void emptyPrompt();

    void clearScreen();

    @NotNull
    String screenName();

    void screenName(@NotNull String name);

    int width();

    int displayLength(@NotNull String string);
}
