package de.natrox.common.concurrent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.TimeUnit;

public sealed interface TaskBatch permits SimpleTaskBatch {

    @NotNull TaskBatch sync(@NotNull Runnable runnable);

    @NotNull TaskBatch async(@NotNull Runnable runnable);

    @NotNull TaskBatch wait(long delay, @NotNull TimeUnit timeUnit);

    void execute(@Nullable Runnable callback);

    default void execute() {
        execute(null);
    }

    @NotNull List<Runnable> interrupt();

}
