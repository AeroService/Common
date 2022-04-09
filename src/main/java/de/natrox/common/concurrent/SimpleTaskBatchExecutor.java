package de.natrox.common.concurrent;

import com.google.common.base.Preconditions;
import de.natrox.common.runnable.CatchingRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

final class SimpleTaskBatchExecutor implements TaskBatchExecutor {

    private final ExecutorService executor;

    protected SimpleTaskBatchExecutor() {
        this.executor = Executors.newSingleThreadExecutor(runnable -> {
            var thread = new Thread(runnable);
            thread.setName("Task Batch");
            return thread;
        });
    }

    @Override
    public void async(@NotNull Runnable runnable) {
        Preconditions.checkNotNull(runnable, "runnable");
        this.executor.submit(new CatchingRunnable(runnable));
    }

    @Override
    public void sync(@NotNull Runnable runnable) {
        Preconditions.checkNotNull(runnable, "runnable");
        runnable.run();
    }

    @Override
    public List<Runnable> interrupt() {
        return this.executor.shutdownNow();
    }

    @Override
    public void shutdown() {
        this.executor.shutdown();
    }
}
