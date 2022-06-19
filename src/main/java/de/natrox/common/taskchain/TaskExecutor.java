package de.natrox.common.taskchain;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

/**
 * Represents an executor for tasks of {@link TaskChain}.
 */
public interface TaskExecutor {

    /**
     * Returns whether the current thread is the main thread or not.
     *
     * @return true, if the current thread is the main thread and false if not
     */
    boolean isMainThread();

    /**
     * Schedule a runnable to run on the main thread.
     *
     * @param runnable the runnable to run
     */
    void executeInMain(@NotNull Runnable runnable);

    /**
     * Run the runnable in a new thread.
     *
     * @param runnable the runnable to run
     */
    void executeAsync(@NotNull Runnable runnable);

    /**
     * Schedule a runnable with delay.
     *
     * @param runnable the runnable to run
     * @param delay the time to delay by
     * @param timeUnit the unit of time for {@code time}
     */
    void executeWithDelay(@NotNull Runnable runnable, long delay, @NotNull TimeUnit timeUnit);

    void shutdown();

}
