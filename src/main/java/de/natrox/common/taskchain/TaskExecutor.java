/*
 * Copyright 2020-2022 NatroxMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.natrox.common.taskchain;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

/**
 * Represents an executor for tasks of {@link TaskChain}.
 */
public interface TaskExecutor {

    /**
     * Returns whether the current thread is the main thread or not.
     *
     * @return true, if the current thread is the main thread and false if not
     */
    boolean isMainThread();

    /**
     * Schedule a runnable to run on the main thread.
     *
     * @param runnable the runnable to run
     */
    void executeInMain(@NotNull Runnable runnable);

    /**
     * Run the runnable in a new thread.
     *
     * @param runnable the runnable to run
     */
    void executeAsync(@NotNull Runnable runnable);

    /**
     * Schedule a runnable with delay.
     *
     * @param runnable the runnable to run
     * @param delay the time to delay by
     * @param timeUnit the unit of time for {@code time}
     */
    void executeWithDelay(@NotNull Runnable runnable, long delay, @NotNull TimeUnit timeUnit);

    /**
     * Shutdowns the executor.
     */
    void shutdown();

}
