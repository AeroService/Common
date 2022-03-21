package de.natrox.common.scheduler;

import de.natrox.common.Loadable;
import de.natrox.common.Shutdownable;
import de.natrox.common.runnable.CatchingRunnable;
import de.natrox.common.system.SystemLoadable;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Scheduler implements Loadable, Shutdownable {

    private final ScheduledExecutorService scheduledExecutorService;

    public Scheduler() {
        this.scheduledExecutorService = Executors.newScheduledThreadPool(4, new DefaultThreadFactory("Scheduler"));
    }

    public ScheduledFuture<?> interval(@NotNull Runnable task, long delay, long interval) {
        Objects.requireNonNull(task, "task can't be null");
        return scheduledExecutorService.scheduleAtFixedRate(new CatchingRunnable(task), delay * 50, interval * 50, TimeUnit.MILLISECONDS);
    }

    public ScheduledFuture<?> interval(@NotNull Runnable task, long delay, long interval, @NotNull TimeUnit timeUnit) {
        Objects.requireNonNull(task, "task can't be null");
        Objects.requireNonNull(timeUnit, "timeUnit can't be null");
        return scheduledExecutorService.scheduleAtFixedRate(new CatchingRunnable(task), delay, interval, timeUnit);
    }


    public ScheduledFuture<?> delay(@NotNull Runnable task, long delay) {
        Objects.requireNonNull(task, "task can't be null");
        return scheduledExecutorService.schedule(new CatchingRunnable(task), delay * 50, TimeUnit.MILLISECONDS);
    }

    public ScheduledFuture<?> delay(@NotNull Runnable task, long delay, @NotNull TimeUnit timeUnit) {
        Objects.requireNonNull(task, "task can't be null");
        Objects.requireNonNull(timeUnit, "timeUnit can't be null");
        return scheduledExecutorService.schedule(new CatchingRunnable(task), delay, timeUnit);
    }

    public void async(@NotNull Runnable task) {
        Objects.requireNonNull(task, "task can't be null");
        scheduledExecutorService.execute(new CatchingRunnable(task));
    }

    public void waitUntilShutdown() {
        shutdown();
        System.out.println("&6Waiting 20s for Scheduler to shut down&7!"); //DEBUG
        try {
            scheduledExecutorService.awaitTermination(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.out.println("&cScheduler was interrupted&7!"); //DEBUG
            e.printStackTrace();
        }
    }

    @Override
    public void load() {

    }

    @Override
    public boolean isLoaded() {
        return true;
    }

    @Override
    public void shutdown() {
        System.out.println("&6Shutting down Scheduler&7!"); //DEBUG
        scheduledExecutorService.shutdown();
    }
}