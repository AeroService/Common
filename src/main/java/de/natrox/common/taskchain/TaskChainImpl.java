package de.natrox.common.taskchain;

import de.natrox.common.validate.Check;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

final class TaskChainImpl implements TaskChain {

    private final TaskExecutor taskExecutor;
    private final Queue<TaskContainer> taskQueue = new ConcurrentLinkedQueue<>();

    private boolean async = false;
    private boolean executed = false;
    private boolean done = false;

    private Runnable doneCallback;
    private TaskContainer currentContainer;

    TaskChainImpl(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Override
    public @NotNull TaskChain sync(@NotNull Task task) {
        Check.notNull(task, "task");
        return this.add(new TaskContainer(this, ExecutionType.SYNC, task));
    }

    @Override
    public @NotNull TaskChain async(@NotNull Task task) {
        Check.notNull(task, "task");
        return this.add(new TaskContainer(this, ExecutionType.ASYNC, task));
    }

    @Override
    public @NotNull TaskChain current(@NotNull Task task) {
        Check.notNull(task, "task");
        return this.add(new TaskContainer(this, ExecutionType.CURRENT, task));
    }

    @Override
    public @NotNull TaskChain delay(@Range(from = 0, to = Long.MAX_VALUE) long duration, @NotNull TimeUnit timeUnit) {
        Check.notNull(timeUnit, "timeUnit");
        return this;
    }

    private TaskChain add(@NotNull TaskContainer task) {
        synchronized (this) {
            if (this.executed) {
                throw new IllegalStateException("This TaskChain has already been executed");
            }
        }

        this.taskQueue.add(task);
        return this;
    }

    @Override
    public void run(@Nullable Runnable callback) {
        this.doneCallback = callback;

        synchronized (this) {
            if (this.executed) {
                throw new IllegalStateException("This TaskChain has already been executed");
            }
            this.executed = true;
        }

        this.async = !this.taskExecutor.isMainThread();
        this.runNextTask();
    }

    private void runNextTask() {
        synchronized (this) {
            this.currentContainer = this.taskQueue.poll();
            if (this.currentContainer == null) {
                this.done = true;
            }
        }

        if (this.currentContainer == null) {
            this.done();
            return;
        }

        ExecutionType executionType = this.currentContainer.executionType;
        if (executionType.equals(ExecutionType.CURRENT)) {
            this.currentContainer.run();
        } else if (executionType.equals(ExecutionType.ASYNC)) {
            if (this.async) {
                this.currentContainer.run();
            } else {
                this.taskExecutor.executeAsync(() -> {
                    this.async = true;
                    this.currentContainer.run();
                });
            }
        } else {
            if (this.async) {
                this.taskExecutor.executeInMain(() -> {
                    this.async = false;
                    this.currentContainer.run();
                });
            } else {
                this.currentContainer.run();
            }
        }
    }

    private void done() {
        this.done = true;
        if (this.doneCallback == null) {
            return;
        }

        this.doneCallback.run();
    }

    private enum ExecutionType {

        ASYNC,
        SYNC,
        CURRENT

    }

    private static class TaskContainer {

        private final TaskChainImpl taskChain;
        private final ExecutionType executionType;
        private final Task task;

        private boolean executed = false;

        private TaskContainer(TaskChainImpl taskChain, ExecutionType executionType, Task task) {
            this.taskChain = taskChain;
            this.executionType = executionType;
            this.task = task;
        }

        private void run() {
            this.task.run();
            this.next();
        }

        private void next() {
            synchronized (this) {
                if (this.executed) {
                    this.taskChain.done();
                    throw new IllegalStateException("This task has already been executed");
                }
                this.executed = true;
            }

            this.taskChain.async = !taskChain.taskExecutor.isMainThread();
            this.taskChain.runNextTask();
        }

    }
}
