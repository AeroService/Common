package de.natrox.common.scheduler;

import org.jetbrains.annotations.NotNull;

public interface Scheduler {

    @NotNull
    static Scheduler create() {
        return new SchedulerImpl();
    }

    @NotNull
    Task.Builder buildTask(@NotNull Runnable task);

    boolean shutdown() throws InterruptedException;

}
