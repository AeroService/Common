/*
 * Copyright 2020-2023 AeroService
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

package org.aero.common.task.scheduler;

/**
 * Represents the different statuses for a {@link Task} of {@link Scheduler}.
 */
public enum TaskStatus {

    /**
     * The singleton instance for the status while the task is scheduled and is currently running.
     */
    SCHEDULED,
    /**
     * The singleton instance for the status when the task was cancelled with {@link Task#cancel()}.
     */
    CANCELLED,
    /**
     * The singleton instance for the status when task has run to completion. This is applicable only for tasks without
     * a repeat.
     */
    FINISHED

}
