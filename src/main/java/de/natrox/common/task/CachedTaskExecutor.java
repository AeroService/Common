package de.natrox.common.task;

import org.jetbrains.annotations.NotNull;

public interface CachedTaskExecutor extends TaskExecutor {

    static @NotNull TaskExecutor create() {
        return new CachedTaskExecutorImpl();
    }
}
