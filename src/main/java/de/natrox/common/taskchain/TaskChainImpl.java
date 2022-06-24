/*
 * Copyright 2020-2022 NatroxMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.natrox.common.taskchain;

import de.natrox.common.taskchain.exception.AbortException;
import de.natrox.common.validate.Check;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

final class TaskChainImpl implements TaskChain {

    private final TaskExecutor taskExecutor;
    private final Queue<TaskContainer> taskQueue = new ConcurrentLinkedQueue<>();

    private boolean async = false;
    private boolean executed = false;
    private boolean done = false;

    private Consumer<Boolean> doneCallback;
    private TaskContainer currentContainer;

    private TaskChainImpl(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Override
    public @NotNull TaskChain abort() {
        if (this.executed) {
            this.throwAbortException();
            return this;
        }

        return this.current(this::throwAbortException);
    }

    private void throwAbortException() {
        throw new AbortException();
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
    public void run(@Nullable Consumer<Boolean> callback) {
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
            this.done(true);
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

    private void done(boolean finished) {
        this.done = true;
        if (this.doneCallback == null) {
            return;
        }

        this.doneCallback.accept(finished);
    }

    private void abortExecuted() {
        this.taskQueue.clear();
        this.done(false);
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
        private boolean aborted = false;

        private TaskContainer(TaskChainImpl taskChain, ExecutionType executionType, Task task) {
            this.taskChain = taskChain;
            this.executionType = executionType;
            this.task = task;
        }

        private void run() {
            try {
                if (this.task instanceof Task.FutureTask futureTask) {
                    CompletableFuture<?> future = futureTask.runFuture();
                    future.whenComplete((r, throwable) -> {
                        if (throwable != null) {
                            this.abort();
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
            }catch (Throwable throwable) {
                this.abort();
            }
        }

        private synchronized void abort() {
            this.aborted = true;
            this.taskChain.abortExecuted();
        }

        private void next() {
            synchronized (this) {
                if (this.executed) {
                    this.taskChain.done(false);
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
                if(this.taskExecutor.isShutdown()) {
                    this.shutdown = true;
                }

                if (this.shutdown) {
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
