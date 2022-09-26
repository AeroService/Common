/*
 * Copyright 2020-2022 Conelux
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

package org.conelux.common.taskchain;

import org.conelux.common.task.TaskExecutor;
import org.conelux.common.validate.Check;
import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * Represents a chain of {@link Task}s.
 */
public sealed interface TaskChain permits TaskChainImpl {

    /**
     * Creates a new {@link TaskChain.Factory}, that can create new a task chain, that schedules the tasks with the
     * specified {@link TaskExecutor}.
     *
     * @param taskExecutor the task executor that schedules the tasks
     * @return the created task chain factory
     */
    static TaskChain.@NotNull Factory createFactory(@NotNull TaskExecutor taskExecutor) {
        Check.notNull(taskExecutor, "taskExecutor");
        return new TaskChainImpl.FactoryImpl(taskExecutor);
    }

    /**
     * Stops the chain as soon as this step is reached. This is especially useful if you want to build the steps of the
     * chain dynamically and stop the chain under certain conditions.
     *
     * @return this task chain, for chaining
     */
    @NotNull TaskChain abort();

    /**
     * Execute the task on the main thread
     *
     * @param task the task to execute
     * @return this task chain, for chaining
     */
    @NotNull TaskChain sync(@NotNull Task task);

    /**
     * Execute the task on an extra thread
     *
     * @param task the task to execute
     * @return this task chain, for chaining
     */
    @NotNull TaskChain async(@NotNull Task task);

    /**
     * Execute the task on the current thread
     *
     * @param task the task to execute
     * @return this task chain, for chaining
     */
    @NotNull TaskChain current(@NotNull Task task);

    /**
     * Execute the task on the main thread
     *
     * @param futureTask the task to execute
     * @return this task chain, for chaining
     */
    @NotNull TaskChain syncFuture(Task.@NotNull FutureTask futureTask);

    /**
     * Execute the task on an extra thread
     *
     * @param futureTask the task to execute
     * @return this task chain, for chaining
     */
    @NotNull TaskChain asyncFuture(Task.@NotNull FutureTask futureTask);

    /**
     * Execute the task on the current thread
     *
     * @param futureTask the task to execute
     * @return this task chain, for chaining
     */
    @NotNull TaskChain currentFuture(Task.@NotNull FutureTask futureTask);

    /**
     * Execute the task on the main thread
     *
     * @param callbackTask the task to execute
     * @return this task chain, for chaining
     */
    @NotNull TaskChain syncCallback(Task.@NotNull CallbackTask callbackTask);

    /**
     * Execute the task on an extra thread
     *
     * @param callbackTask the task to execute
     * @return this task chain, for chaining
     */
    @NotNull TaskChain asyncCallback(Task.@NotNull CallbackTask callbackTask);

    /**
     * Execute the task on the current thread
     *
     * @param callbackTask the task to execute
     * @return this task chain, for chaining
     */
    @NotNull TaskChain currentCallback(Task.@NotNull CallbackTask callbackTask);

    /**
     * Adds a delay to the batch execution.
     *
     * @param duration the time to delay by
     * @param timeUnit the unit of time for {@code time}
     * @return this task chain, for chaining
     */
    @NotNull TaskChain delay(@Range(from = 0, to = Long.MAX_VALUE) long duration, @NotNull TimeUnit timeUnit);

    /**
     * Adds a delay to the batch execution.
     *
     * @param duration the duration of the delay
     * @return this task chain, for chaining
     */
    default @NotNull TaskChain delay(@NotNull Duration duration) {
        Check.notNull(duration, "duration");
        return this.delay(duration.toMillis(), TimeUnit.MILLISECONDS);
    }

    /**
     * Adds a delay to the batch execution.
     *
     * @param duration     the time to delay by
     * @param temporalUnit the unit of time for {@code time}
     * @return this task chain, for chaining
     */
    default @NotNull TaskChain delay(@Range(from = 0, to = Long.MAX_VALUE) long duration,
        @NotNull TemporalUnit temporalUnit) {
        Check.notNull(temporalUnit, "temporalUnit");
        return this.delay(Duration.of(duration, temporalUnit));
    }

    /**
     * Finished adding tasks, begins executing them and calls a callback when the task has finished.
     *
     * @param callback the callback
     */
    void run(@Nullable Consumer<Boolean> callback);

    /**
     * Finished adding tasks, begins executing them and calls a callback when the task has finished.
     *
     * @param callback the callback
     */
    default void run(@Nullable Runnable callback) {
        this.run(callback != null ? result -> callback.run() : null);
    }

    /**
     * Finished adding tasks, begins executing them.
     */
    default void run() {
        this.run((Consumer<Boolean>) null);
    }

    /**
     * Represents a factory for {@link TaskChain}s.
     */
    interface Factory {

        /**
         * Creates a new task chain.
         *
         * @return the created task chain
         */
        @NotNull TaskChain create();

    }

}
