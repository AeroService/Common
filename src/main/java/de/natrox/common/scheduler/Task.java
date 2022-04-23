/*
 * Copyright 2020-2022 NatroxMC team
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

package de.natrox.common.scheduler;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Represents a task that is scheduled to run.
 */
public sealed interface Task permits TaskImpl {

    /**
     * Returns the scheduler on which the task is running.
     *
     * @return the scheduler on which the task is running.
     */
    @NotNull Scheduler owner();

    /**
     * Returns the current status of this task.
     *
     * @return the current status of this task
     */
    @NotNull TaskStatus status();

    /**
     * Cancels this task. If the task is already running, the thread in which it is running will be
     * interrupted. If the task is not currently running, the scheduler will terminate it safely.
     */
    void cancel();

    /**
     * Represents a fluent interface to schedule tasks.
     */
    sealed interface Builder permits TaskBuilderImpl {

        /**
         * Specifies that the task should delay its execution by the specified amount of time.
         *
         * @param time     the time to delay by
         * @param timeUnit the unit of time for {@code time}
         * @return this builder, for chaining
         */
        @NotNull Builder delay(long time, @NotNull TimeUnit timeUnit);

        /**
         * Specifies that the task should delay its execution by the specified amount of time.
         *
         * @param duration the duration of the delay
         * @return this builder, for chaining
         */
        default @NotNull Builder delay(Duration duration) {
            return delay(duration.toMillis(), TimeUnit.MILLISECONDS);
        }

        /**
         * Specifies that the task should continue running after waiting for the specified amount, until
         * it is cancelled.
         *
         * @param time     the time to delay by
         * @param timeUnit the unit of time for {@code time}
         * @return this builder, for chaining
         */
        @NotNull Builder repeat(long time, @NotNull TimeUnit timeUnit);

        /**
         * Specifies that the task should continue running after waiting for the specified amount, until
         * it is cancelled.
         *
         * @param duration the duration of the delay
         * @return this builder, for chaining
         */
        default @NotNull Builder repeat(Duration duration) {
            return repeat(duration.toMillis(), TimeUnit.MILLISECONDS);
        }

        /**
         * Clears the delay on this task.
         *
         * @return this builder, for chaining
         */
        @NotNull Builder clearDelay();

        /**
         * Clears the repeat interval on this task.
         *
         * @return this builder, for chaining
         */
        @NotNull Builder clearRepeat();

        /**
         * Schedules this task for execution.
         *
         * @return the scheduled task
         */
        @NotNull Task schedule();

    }

}
