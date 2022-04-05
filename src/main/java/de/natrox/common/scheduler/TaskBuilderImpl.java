package de.natrox.common.scheduler;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

final class TaskBuilderImpl implements Task.Builder {

    private final SchedulerImpl scheduler;
    private final Runnable runnable;
    private long delay; // ms
    private long repeat; // ms

    protected TaskBuilderImpl(SchedulerImpl scheduler, Runnable runnable) {
        this.scheduler = scheduler;
        this.runnable = runnable;
    }

    @Override
    public @NotNull TaskBuilderImpl delay(long time, TimeUnit unit) {
        this.delay = unit.toMillis(time);
        return this;
    }

    @Override
    public @NotNull TaskBuilderImpl repeat(long time, TimeUnit unit) {
        this.repeat = unit.toMillis(time);
        return this;
    }

    @Override
    public @NotNull TaskBuilderImpl clearDelay() {
        this.delay = 0;
        return this;
    }

    @Override
    public @NotNull TaskBuilderImpl clearRepeat() {
        this.repeat = 0;
        return this;
    }

    @Override
    public @NotNull Task schedule() {
        var task = new TaskImpl(scheduler, runnable, delay, repeat);
        task.schedule();
        return task;
    }
}
