package de.natrox.common.console;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jline.reader.EndOfFileException;
import org.jline.reader.UserInterruptException;

import java.util.concurrent.CompletableFuture;

public class ConsoleReadThread extends Thread {

    private final JLine3Console console;
    private CompletableFuture<String> currentTask;

    public ConsoleReadThread(JLine3Console console) {
        this.console = console;
    }

    @Override
    public void run() {
        String line;
        while (!Thread.interrupted() && (line = this.readLine()) != null) {
            if (this.currentTask != null) {
                this.currentTask.complete(line);
                this.currentTask = null;
            }

            for (var value : this.console.consoleInputHandler().values()) {
                if (value.enabled()) {
                    value.handleInput(line);
                }
            }
        }
    }

    private @Nullable String readLine() {
        try {
            return this.console.lineReader().readLine(this.console.prompt());
        } catch (EndOfFileException ignored) {
        } catch (UserInterruptException exception) {
            System.exit(-1);
        }

        return null;
    }

    protected @NotNull CompletableFuture<String> currentTask() {
        if (this.currentTask == null) {
            this.currentTask = new CompletableFuture<>();
        }

        return this.currentTask;
    }
}
