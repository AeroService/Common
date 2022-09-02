/*
 * Copyright 2020-2022 NatroxMC
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.natrox.common.counter;

import de.natrox.common.builder.IBuilder;
import de.natrox.common.task.TaskExecutor;
import de.natrox.common.validate.Check;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * Represents a clocked Counter, for example a countdown.
 */
public sealed interface Counter permits CounterImpl {

    /**
     * Create a {@link Builder} for a counter, that schedules the tasks with the specified {@link TaskExecutor}.
     *
     * @param taskExecutor the task executor that schedules the tasks
     * @return the created builder
     */
    static Counter.@NotNull Builder builder(@NotNull TaskExecutor taskExecutor) {
        Check.notNull(taskExecutor, "taskExecutor");
        return new CounterImpl.BuilderImpl(taskExecutor);
    }

    /**
     * Starts this counter if its status is {@link CounterStatus#IDLING} and sets the status to
     * {@link CounterStatus#RUNNING}.
     */
    void start();

    /**
     * Pauses this counter if its status is {@link CounterStatus#RUNNING} and sets the status to
     * {@link CounterStatus#PAUSED}.
     */
    void pause();

    /**
     * Resumes this counter if its status is {@link CounterStatus#PAUSED} and sets the status to
     * {@link CounterStatus#RUNNING}.
     */
    void resume();

    /**
     * Stops this counter if its status not {@link CounterStatus#IDLING} and sets the status to
     * {@link CounterStatus#IDLING}.
     */
    void stop();

    /**
     * Returns whether this counter is paused or not, respectively if the status of this counter is
     * {@link CounterStatus#PAUSED}.
     *
     * @return true, if this counter is paused, false, if not
     */
    boolean isPaused();

    /**
     * Returns whether this counter is running or not, respectively if the status of this counter is
     * {@link CounterStatus#RUNNING}.
     *
     * @return true, if this counter is running, false, if not
     */
    boolean isRunning();

    /**
     * Returns the current {@link CounterStatus}.
     *
     * @return the counter status
     */
    @NotNull CounterStatus status();

    /**
     * Returns the value at which this counter starts.
     *
     * @return the start value
     */
    long startCount();

    /**
     * Returns the value at which this counter stops.
     *
     * @return the stop value
     */
    long stopCount();

    /**
     * Returns the value of a single tick.
     *
     * @return the tick value
     */
    long tickValue();

    /**
     * Returns the {@link TimeUnit} in which the value of a single tick is specified.
     *
     * @return the time unit
     * @see #tickValue()
     */
    @NotNull TimeUnit tickUnit();

    /**
     * Returns the value of the ticks this counter has done since the last start.
     *
     * @return the tick amount
     */
    long tickedCount();

    /**
     * Returns the current count value of this counter.
     *
     * @return the current count
     */
    long currentCount();

    /**
     * Sets the current count value to the specified value.
     *
     * @param count the value to set
     */
    void setCurrentCount(long count);

    /**
     * Represents a builder for a {@link Counter}.
     */
    sealed interface Builder extends IBuilder<Counter> permits CounterImpl.BuilderImpl {

        /**
         * Sets the value at which the counter should start.
         *
         * @param startCount the start value
         * @return this builder, for chaining
         */
        @NotNull Builder startCount(@Range(from = Long.MIN_VALUE, to = Long.MAX_VALUE) long startCount);

        /**
         * Sets the value at which this counter should stop.
         *
         * @param stopCount the stop value
         * @return this builder, for chaining
         */
        @NotNull Builder stopCount(@Range(from = Long.MIN_VALUE, to = Long.MAX_VALUE) long stopCount);

        /**
         * Sets the delay between two ticks.
         *
         * @param tick     the time to delay by
         * @param tickUnit the unit of time for {@code time}
         * @return this builder, for chaining
         */
        @NotNull Builder tick(@Range(from = 0, to = Long.MAX_VALUE) long tick, @NotNull TimeUnit tickUnit);

        /**
         * Sets the delay between two ticks.
         *
         * @param tick     the time to delay by
         * @param tickUnit the unit of time for {@code time}
         * @return this builder, for chaining
         */
        default @NotNull Builder tick(@Range(from = 0, to = Long.MAX_VALUE) long tick, @NotNull ChronoUnit tickUnit) {
            Check.argCondition(tick <= 0, "tick must be positive");
            Check.notNull(tickUnit, "tickUnit");
            return tick(tick, TimeUnit.of(tickUnit));
        }

        /**
         * Sets a callback which gets called when the counter is started.
         *
         * @param startCallback the callback
         * @return this builder, for chaining
         */
        @NotNull Builder startCallback(@Nullable Consumer<Counter> startCallback);

        /**
         * Sets a callback which gets called when the counter ticks.
         *
         * @param tickCallback the callback
         * @return this builder, for chaining
         */
        @NotNull Builder tickCallback(@Nullable Consumer<Counter> tickCallback);

        /**
         * Sets a callback which gets called when the counter is finished.
         *
         * @param finishCallback the callback
         * @return this builder, for chaining
         */
        @NotNull Builder finishCallback(@Nullable Consumer<Counter> finishCallback);

        /**
         * Sets a callback which gets called when the counter is cancelled.
         *
         * @param cancelCallback the callback
         * @return this builder, for chaining
         */
        @NotNull Builder cancelCallback(@Nullable Consumer<Counter> cancelCallback);

        /**
         * Returns the {@link Counter} that should be built.
         *
         * @return the built counter
         */
        @Override
        @NotNull Counter build();

    }
}
