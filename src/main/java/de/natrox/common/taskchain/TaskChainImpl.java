package de.natrox.common.taskchain;

import de.natrox.common.validate.Check;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Queue;
import java.util.concurrent.CompletableFuture;
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

    private TaskChainImpl(TaskExecutor taskExecutor) {
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
    public @NotNull TaskChain syncFuture(Task.@NotNull FutureTask futureTask) {
        Check.notNull(futureTask, "futureTask");
        return this.add(new TaskContainer(this, ExecutionType.SYNC, futureTask));
    }

    @Override
    public @NotNull TaskChain asyncFuture(Task.@NotNull FutureTask futureTask) {
        Check.notNull(futureTask, "futureTask");
        return this.add(new TaskContainer(this, ExecutionType.ASYNC, futureTask));
    }

    @Override
    public @NotNull TaskChain currentFuture(Task.@NotNull FutureTask futureTask) {
        Check.notNull(futureTask, "futureTask");
        return this.add(new TaskContainer(this, ExecutionType.CURRENT, futureTask));
    }

    @Override
    public @NotNull TaskChain syncCallback(Task.@NotNull CallbackTask callbackTask) {
        Check.notNull(callbackTask, "callbackTask");
        return this.add(new TaskContainer(this, ExecutionType.SYNC, callbackTask));
    }

    @Override
    public @NotNull TaskChain asyncCallback(Task.@NotNull CallbackTask callbackTask) {
        Check.notNull(callbackTask, "callbackTask");
        return this.add(new TaskContainer(this, ExecutionType.ASYNC, callbackTask));
    }

    @Override
    public @NotNull TaskChain currentCallback(Task.@NotNull CallbackTask callbackTask) {
        Check.notNull(callbackTask, "callbackTask");
        return this.add(new TaskContainer(this, ExecutionType.CURRENT, callbackTask));
    }

    @Override
    public @NotNull TaskChain delay(@Range(from = 0, to = Long.MAX_VALUE) long duration, @NotNull TimeUnit timeUnit) {
        Check.notNull(timeUnit, "timeUnit");
        return this.currentCallback(callback -> this.taskExecutor.executeWithDelay(callback, duration, timeUnit));
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
            if(this.task instanceof Task.FutureTask futureTask) {
                CompletableFuture<?> future = futureTask.runFuture();
                future.whenComplete((r, throwable) -> {
                    if (throwable != null) {
                        //TODO:
                    } else {
                        this.next();
                    }
                });
            } else if (this.task instanceof Task.CallbackTask callbackTask) {
                callbackTask.run(this::next);
            } else {
                this.task.run();
                this.next();
            }
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

    static class FactoryImpl implements TaskChain.Factory {

        private final TaskExecutor taskExecutor;
        private boolean shutdown = false;

        FactoryImpl(TaskExecutor taskExecutor) {
            this.taskExecutor = taskExecutor;
        }

        @Override
        public @NotNull TaskChain create() {
            synchronized (this) {
                if(this.shutdown) {
                    throw new IllegalStateException("This factory has already been closed");
                }
            }
            return new TaskChainImpl(this.taskExecutor);
        }

        @Override
        public void shutdown() {
            synchronized (this) {
                this.shutdown = true;
            }
            this.taskExecutor.shutdown();
        }
    }
}
