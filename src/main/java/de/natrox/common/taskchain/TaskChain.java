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

import de.natrox.common.task.TaskExecutor;
import de.natrox.common.validate.Check;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Represents a chain of tasks
 */
public sealed interface TaskChain permits TaskChainImpl {

    /**
     * Creates a new task chain factory.
     *
     * @param taskExecutor the {@link TaskExecutor} which should execute the tasks of the task chain
     * @return the created task chain factory
     */
    static TaskChain.@NotNull Factory createFactory(@NotNull TaskExecutor taskExecutor) {
        Check.notNull(taskExecutor, "taskExecutor");
        return new TaskChainImpl.FactoryImpl(taskExecutor);
    }

    /**
     * Stops the chain as soon as this step is reached. This is especially
     * useful if you want to build the steps of the chain
     * dynamically and stop the chain under certain conditions.
     *
     * @return this TaskChain, for chaining
     */
    @NotNull TaskChain abort();

    /**
     * Execute the task on the main thread
     *
     * @param task the task to execute
     * @return this TaskChain, for chaining
     */
    @NotNull TaskChain sync(@NotNull Task task);

    /**
     * Execute the task on an extra thread
     *
     * @param task the task to execute
     * @return this TaskChain, for chaining
     */
    @NotNull TaskChain async(@NotNull Task task);

    /**
     * Execute the task on the current thread
     *
     * @param task the task to execute
     * @return this TaskChain, for chaining
     */
    @NotNull TaskChain current(@NotNull Task task);

    /**
     * Execute the task on the main thread
     *
     * @param futureTask the task to execute
     * @return this TaskChain, for chaining
     */
    @NotNull TaskChain syncFuture(Task.@NotNull FutureTask futureTask);

    /**
     * Execute the task on an extra thread
     *
     * @param futureTask the task to execute
     * @return this TaskChain, for chaining
     */
    @NotNull TaskChain asyncFuture(Task.@NotNull FutureTask futureTask);

    /**
     * Execute the task on the current thread
     *
     * @param futureTask the task to execute
     * @return this TaskChain, for chaining
     */
    @NotNull TaskChain currentFuture(Task.@NotNull FutureTask futureTask);

    /**
     * Execute the task on the main thread
     *
     * @param callbackTask the task to execute
     * @return this TaskChain, for chaining
     */
    @NotNull TaskChain syncCallback(Task.@NotNull CallbackTask callbackTask);

    /**
     * Execute the task on an extra thread
     *
     * @param callbackTask the task to execute
     * @return this TaskChain, for chaining
     */
    @NotNull TaskChain asyncCallback(Task.@NotNull CallbackTask callbackTask);

    /**
     * Execute the task on the current thread
     *
     * @param callbackTask the task to execute
     * @return this TaskChain, for chaining
     */
    @NotNull TaskChain currentCallback(Task.@NotNull CallbackTask callbackTask);

    /**
     * Adds a delay to the batch execution.
     *
     * @param duration the duration of the delay before next task
     * @param timeUnit the {@link TimeUnit} in which the duration is specified
     * @return this TaskChain, for chaining
     */
    @NotNull TaskChain delay(@Range(from = 0, to = Long.MAX_VALUE) long duration, @NotNull TimeUnit timeUnit);

    /**
     * Adds a delay to the batch execution.
     *
     * @param duration the {@link Duration} of the delay before next task
     * @return this TaskChain, for chaining
     */
    default @NotNull TaskChain delay(@NotNull Duration duration) {
        Check.notNull(duration, "duration");
        return this.delay(duration.toMillis(), TimeUnit.MILLISECONDS);
    }

    /**
     * Adds a delay to the batch execution.
     *
     * @param duration     the duration of the delay before next task
     * @param temporalUnit the {@link TemporalUnit} in which the duration is specified
     * @return this TaskChain, for chaining
     */
    default @NotNull TaskChain delay(@Range(from = 0, to = Long.MAX_VALUE) long duration, @NotNull TemporalUnit temporalUnit) {
        Check.notNull(temporalUnit, "temporalUnit");
        return this.delay(Duration.of(duration, temporalUnit));
    }

    /**
     * Finished adding tasks, begins executing them with a done notifier
     *
     * @param callback the {@link Consumer} to handle when the batch has finished completion
     */
    void run(@Nullable Consumer<Boolean> callback);

    /**
     * Finished adding tasks, begins executing them with a done notifier
     *
     * @param callback the {@link Runnable} to handle when the batch has finished completion
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

    interface Factory {

        /**
         * Creates a new task chain.
         *
         * @return the created task chain
         */
        @NotNull TaskChain create();

    }

}
