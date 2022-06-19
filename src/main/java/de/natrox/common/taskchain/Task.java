package de.natrox.common.taskchain;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public interface Task extends Runnable {

    @Override
    void run();

    interface CallbackTask extends Task {

        @Override
        default void run() {
            this.run(() -> {

            });
        }

        void run(@NotNull Runnable callback);
    }

    interface FutureTask extends Task {

        @Override
        default void run() {
            CompletableFuture<?> future = this.runFuture();
        }

        @NotNull CompletableFuture<?> runFuture();

    }

}
