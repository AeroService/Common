package de.natrox.common.taskchain;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public interface TaskExecutor {

    boolean isMainThread();

    void executeInMain(@NotNull Runnable runnable);

    void executeAsync(@NotNull Runnable runnable);

    void executeWithDelay(@NotNull Runnable runnable, long delay, @NotNull TimeUnit timeUnit);

}
