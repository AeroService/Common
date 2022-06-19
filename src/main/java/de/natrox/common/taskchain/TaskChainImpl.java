package de.natrox.common.taskchain;

import de.natrox.common.validate.Check;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.concurrent.TimeUnit;

final class TaskChainImpl implements TaskChain {

    TaskChainImpl() {

    }

    @Override
    public @NotNull TaskChain sync(@NotNull Runnable runnable) {
        Check.notNull(runnable, "runnable");
        return this;
    }

    @Override
    public @NotNull TaskChain async(@NotNull Runnable runnable) {
        Check.notNull(runnable, "runnable");
        return this;
    }

    @Override
    public @NotNull TaskChain delay(@Range(from = 0, to = Long.MAX_VALUE) long duration, @NotNull TimeUnit timeUnit) {
        Check.notNull(timeUnit, "timeUnit");
        return this;
    }

    @Override
    public void run(@Nullable Runnable callback) {
        this.runNextTask();
    }

    private void runNextTask() {

    }

    private void done() {

    }

    private static class Task {

        private final TaskChainImpl taskChain;

        private Task(TaskChainImpl taskChain) {
            this.taskChain = taskChain;
        }

        private void run() {

        }

        private void next() {
            this.taskChain.runNextTask();
        }

    }
}
