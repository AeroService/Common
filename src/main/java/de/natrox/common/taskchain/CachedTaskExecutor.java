package de.natrox.common.taskchain;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class CachedTaskExecutor implements TaskExecutor {

    private final ExecutorService executorService;

    private CachedTaskExecutor() {
        this.executorService = Executors.newCachedThreadPool(this::createThread);
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
        this.executorService.submit(() -> {
            try {
                Thread.sleep(timeUnit.toMillis(delay));
                runnable.run();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void shutdown() {
        try {
            this.executorService.shutdown();
            this.executorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
