/*
 * Copyright 2020-2022 NatroxMC team
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

import org.jetbrains.annotations.NotNull;

/**
 * Represents a scheduler to execute tasks.
 */
public sealed interface Scheduler permits SchedulerImpl {

    static @NotNull Scheduler create() {
        return new SchedulerImpl();
    }

    /**
     * Initializes a new {@link Task.Builder} for creating a task.
     *
     * @param task the task to run when scheduled
     * @return the task builder
     */
    @NotNull Task.Builder buildTask(@NotNull Runnable task);

    /**
     * Shutdowns the scheduler and cancel all running tasks.
     *
     * @return true if this executor terminated and false if 10 seconds elapsed before termination
     * @throws InterruptedException if interrupted while waiting
     */
    boolean shutdown() throws InterruptedException;

}
