package de.natrox.common.concurrent;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents a executor for a {@link TaskBatch}
 */
public interface TaskBatchExecutor {

    void async(@NotNull Runnable runnable);

    void sync(@NotNull Runnable runnable);

    List<Runnable> interrupt();

    void shutdown();

}
