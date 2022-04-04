package de.natrox.common.concurrent;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface TaskBatchExecutor {

    void async(@NotNull Runnable runnable);

    void sync(@NotNull Runnable runnable);

    List<Runnable> interrupt();

    void shutdown();

}
