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

package de.natrox.common.task;

public sealed interface Task permits TaskImpl.AbstractTask {

    /**
     * Cancels this task. If the task is already running, the thread in which it is running will be interrupted.
     */
    void cancel();

    /**
     * Returns whether the task is cancelled or not.
     *
     * @return true, if the task is cancelled and false if not
     */
    boolean isCancelled();

    /**
     * Returns whether the task is done or not.
     *
     * @return true, if the task is done and false if not
     */
    boolean isDone();

}
