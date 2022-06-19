package de.natrox.common.taskchain;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/**
 * Represents a task that can be executed on any thread.
 */
public interface Task extends Runnable {

    @Override
    void run();

    /**
     * Represents a task that does not return immediately. A supplied Consumer controls when
     * the chain should proceed to the next task. This is a Callback style API
     * in relation to the Future based API.
     */
    interface CallbackTask extends Task {

        @Override
        default void run() {
            this.run(() -> {

            });
        }

        void run(@NotNull Runnable callback);
    }

    /**
     * Represents a task that returns a future to be completed later.
     */
    interface FutureTask extends Task {

        @Override
        default void run() {
            CompletableFuture<?> future = this.runFuture();
        }

        @NotNull CompletableFuture<?> runFuture();

    }

}
