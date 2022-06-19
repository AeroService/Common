package de.natrox.common.taskchain;

import de.natrox.common.validate.Check;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

final class TaskChainImpl implements TaskChain {

    private final Queue<TaskContainer> taskQueue = new ConcurrentLinkedQueue<>();

    private boolean executed = false;

    TaskChainImpl() {

    }

    @Override
    public @NotNull TaskChain sync(@NotNull Runnable runnable) {
        Check.notNull(runnable, "runnable");
        return this.add(new TaskContainer(this, ExecutionType.SYNC));
    }

    @Override
    public @NotNull TaskChain async(@NotNull Runnable runnable) {
        Check.notNull(runnable, "runnable");
        return this.add(new TaskContainer(this, ExecutionType.ASYNC));
    }

    @Override
    public @NotNull TaskChain delay(@Range(from = 0, to = Long.MAX_VALUE) long duration, @NotNull TimeUnit timeUnit) {
        Check.notNull(timeUnit, "timeUnit");
        return this;
    }

    private TaskChain add(@NotNull TaskContainer task) {
        synchronized (this) {
            if (this.executed) {
                throw new IllegalStateException("TaskChain was already executed");
            }
        }

        this.taskQueue.add(task);
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

    private enum ExecutionType {

        ASYNC,
        SYNC

    }

    private static class TaskContainer {

        private final TaskChainImpl taskChain;
        private final ExecutionType executionType;

        private TaskContainer(TaskChainImpl taskChain, ExecutionType executionType) {
            this.taskChain = taskChain;
            this.executionType = executionType;
        }

        private void run() {

        }

        private void next() {
            this.taskChain.runNextTask();
        }

    }
}
