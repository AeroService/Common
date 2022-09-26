/*
 * Copyright 2020-2022 Conelux
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

package org.conelux.common.task;

import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an executor for {@link Task}s.
 */
public interface TaskExecutor {

    /**
     * Returns whether the current thread is the main thread or not.
     *
     * @return true, if the current thread is the main thread, false, if not
     */
    boolean isMainThread();

    /**
     * Schedule a runnable to run on the main thread.
     *
     * @param runnable the runnable to run
     * @return the {@link Task}
     */
    @NotNull Task executeInMain(@NotNull Runnable runnable);

    /**
     * Run the runnable in a new thread.
     *
     * @param runnable the runnable to run
     * @return the {@link Task}
     */
    @NotNull Task executeAsync(@NotNull Runnable runnable);

    /**
     * Schedule a runnable with delay.
     *
     * @param runnable the runnable to run
     * @param delay    the time to delay by
     * @param timeUnit the unit of time for {@code time}
     * @return the {@link Task}
     */
    @NotNull Task executeWithDelay(@NotNull Runnable runnable, long delay, @NotNull TimeUnit timeUnit);

    /**
     * Schedule a runnable in repetition.
     *
     * @param runnable     the runnable to run
     * @param initialDelay the time to delay the first run
     * @param delay        the time to delay the next run
     * @param timeUnit     the unit of time for {@code time}
     * @return the {@link Task}
     */
    @NotNull Task executeInRepeat(@NotNull Runnable runnable, long initialDelay, long delay,
        @NotNull TimeUnit timeUnit);

    /**
     * Returns whether this task executor is shut down or not.
     *
     * @return true, if this task executor is shut down, false, if not
     */
    boolean isShutdown();

    /**
     * Shutdowns the executor.
     *
     * @return true if this executor terminated and false if the timeout elapsed before termination
     */
    boolean shutdown();

}
