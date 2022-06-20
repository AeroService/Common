package de.natrox.common.taskchain;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class CachedTaskExecutor implements TaskExecutor {

    private final ExecutorService executorService;
    private final ScheduledExecutorService timerExecutionService;

    private CachedTaskExecutor() {
        this.executorService = Executors.newCachedThreadPool(this::createThread);
        this.timerExecutionService = Executors.newSingleThreadScheduledExecutor(this::createTimerThread);
    }

    public static @NotNull TaskExecutor create() {
        return new CachedTaskExecutor();
    }

    private Thread createThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setName("Task Chain - #" + thread.getId());
        thread.setDaemon(true);
        return thread;
    }

    private Thread createTimerThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setName("Task Chain Timer");
        thread.setDaemon(true);
        return thread;
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
        this.executorService.submit(runnable);
    }

    @Override
    public void executeWithDelay(@NotNull Runnable runnable, long delay, @NotNull TimeUnit timeUnit) {
        this.timerExecutionService.schedule(runnable, delay, timeUnit);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void shutdown() {
        try {
            this.timerExecutionService.shutdownNow();
            this.executorService.shutdown();
            this.executorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
