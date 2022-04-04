package de.natrox.common.concurrent;

import de.natrox.common.runnable.CatchingRunnable;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class SimpleTaskBatchExecutor implements TaskBatchExecutor {

    private final ExecutorService executor;

    protected SimpleTaskBatchExecutor() {
        this.executor = Executors.newSingleThreadExecutor(new DefaultThreadFactory("TaskBatch"));
    }

    @Override
    public void async(@NotNull Runnable runnable) {
        this.executor.submit(new CatchingRunnable(runnable));
    }

    @Override
    public void sync(@NotNull Runnable runnable) {
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
