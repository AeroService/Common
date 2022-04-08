package de.natrox.common.scheduler;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a scheduler to execute tasks.
 */
public interface Scheduler {

    static @NotNull Scheduler create() {
        return new SchedulerImpl();
    }

    /**
     * Initializes a new {@link Task.Builder} for creating a task on the proxy.
     *
     * @param task the task to run when scheduled
     * @return the task builder
     */
    @NotNull Task.Builder buildTask(@NotNull Runnable task);

    /**
     * Shutdowns the scheduler and cancel all running tasks.
     *
     * @return true if this executor terminated and false if 10 seconds elapsed before termination
     * @throws InterruptedException if interrupted while waiting
     */
    boolean shutdown() throws InterruptedException;

}
