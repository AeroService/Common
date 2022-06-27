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

package de.natrox.common.scheduler;

import de.natrox.common.task.TaskExecutor;
import de.natrox.common.validate.Check;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a scheduler to execute tasks.
 */
public sealed interface Scheduler permits SchedulerImpl {

    /**
     * Creates a scheduler, that schedules the tasks with the specified {@link TaskExecutor}.
     *
     * @param taskExecutor the task executor that schedules the tasks
     * @return the created scheduler
     */
    static @NotNull Scheduler create(@NotNull TaskExecutor taskExecutor) {
        Check.notNull(taskExecutor, "taskExecutor");
        return new SchedulerImpl(taskExecutor);
    }

    /**
     * Creates a new {@link Task.Builder} for creating a task.
     *
     * @param task the runnable to run
     * @return the created task builder
     */
    @NotNull Task.Builder buildTask(@NotNull Runnable task);

    /**
     * Returns whether this scheduler is shut down or not.
     *
     * @return true, if this scheduler is shut down, false, if not
     */
    boolean isShutdown();

    /**
     * Shutdowns the scheduler and cancel all running tasks.
     *
     * @return true, if this executor terminated, false, if 10 seconds elapsed before termination
     * @throws InterruptedException if interrupted while waiting
     */
    boolean shutdown() throws InterruptedException;

}
