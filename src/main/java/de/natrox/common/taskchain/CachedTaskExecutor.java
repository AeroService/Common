package de.natrox.common.taskchain;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

final class CachedTaskExecutor implements TaskExecutor {

    private final ExecutorService executorService;

    CachedTaskExecutor() {
        this.executorService = Executors.newCachedThreadPool(this::createThread);
    }

    private Thread createThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setName("Task Chain - #" + thread.getId());
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
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
