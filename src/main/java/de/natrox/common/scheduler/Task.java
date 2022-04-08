package de.natrox.common.scheduler;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Represents a task that is scheduled to run.
 */
public interface Task {

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
    interface Builder {

        /**
         * Specifies that the task should delay its execution by the specified amount of time.
         *
         * @param time the time to delay by
         * @param unit the unit of time for {@code time}
         * @return this builder, for chaining
         */
        @NotNull Builder delay(long time, TimeUnit unit);

        /**
         * Specifies that the task should delay its execution by the specified amount of time.
         *
         * @param duration the duration of the delay
         * @return this builder, for chaining
         */
        @NotNull
        default Builder delay(Duration duration) {
            return delay(duration.toMillis(), TimeUnit.MILLISECONDS);
        }

        /**
         * Specifies that the task should continue running after waiting for the specified amount, until
         * it is cancelled.
         *
         * @param time the time to delay by
         * @param unit the unit of time for {@code time}
         * @return this builder, for chaining
         */
        @NotNull Builder repeat(long time, TimeUnit unit);

        /**
         * Specifies that the task should continue running after waiting for the specified amount, until
         * it is cancelled.
         *
         * @param duration the duration of the delay
         * @return this builder, for chaining
         */
        @NotNull default Builder repeat(Duration duration) {
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
