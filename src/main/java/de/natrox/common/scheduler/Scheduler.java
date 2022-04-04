package de.natrox.common.scheduler;

import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public interface Scheduler {

    @NotNull
    static Scheduler create() {
        return new SchedulerImpl();
    }

    @NotNull Task submitTask(@NotNull Supplier<TaskSchedule> task);

    default @NotNull Task.Builder buildTask(@NotNull Runnable task) {
        return new Task.Builder(this, task);
    }

}
