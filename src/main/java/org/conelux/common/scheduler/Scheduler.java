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

package org.conelux.common.scheduler;

import java.util.function.Supplier;
import org.conelux.common.validate.Check;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a scheduler to execute tasks.
 */
public sealed interface Scheduler permits SchedulerImpl {

    /**
     * Creates a scheduler that schedules the tasks.
     *
     * @return the created scheduler
     */
    static @NotNull Scheduler create() {
        return new SchedulerImpl();
    }

    /**
     * Submits a new task with custom scheduling logic.
     * <p>
     * This is the primitive method used by all scheduling shortcuts,
     * {@code task} is immediately executed in the caller thread to retrieve its scheduling state
     * and the task will stay alive as long as {@link TaskSchedule#stop()} is not returned (or {@link Task#cancel()} is called).
     *
     * @param task          the task to be directly executed in the caller thread
     * @return the created task
     */
    @NotNull Task submitTask(@NotNull Supplier<TaskSchedule> task);

    /**
     * Creates a new {@link Task.Builder} for creating a task.
     *
     * @param runnable the runnable to run
     * @return the created task builder
     */
    default @NotNull Task.Builder buildTask(@NotNull Runnable runnable) {
        Check.notNull(runnable, "runnable");
        return new TaskImpl.BuilderImpl(this, runnable);
    }
}
