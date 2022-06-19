package de.natrox.common.taskchain;

import org.jetbrains.annotations.NotNull;

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

}
