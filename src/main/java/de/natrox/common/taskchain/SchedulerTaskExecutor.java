package de.natrox.common.taskchain;

import de.natrox.common.scheduler.Scheduler;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

final class SchedulerTaskExecutor implements TaskExecutor {

    private final Scheduler scheduler;

    SchedulerTaskExecutor(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public boolean isMainThread() {
        return Thread.currentThread().getId() == 1;
    }

    @Override
    public void executeInMain(@NotNull Runnable runnable) {
        runnable.run();
    }

    @Override
    public void executeAsync(@NotNull Runnable runnable) {
        this.scheduler.buildTask(runnable).schedule();
    }

    @Override
    public void executeWithDelay(@NotNull Runnable runnable, long delay, @NotNull TimeUnit timeUnit) {
        this.scheduler.buildTask(runnable).delay(delay, timeUnit).schedule();
    }
}
