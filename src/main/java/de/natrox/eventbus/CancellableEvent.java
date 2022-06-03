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

package de.natrox.eventbus;

/**
 * Represents an event which can be cancelled.
 */
public interface CancellableEvent {

    /**
     * Gets if the event should be cancelled or not.
     *
     * @return true if the event should be cancelled
     */
    boolean isCancelled();

    /**
     * Marks the event as cancelled or not.
     *
     * @param cancel true if the event should be cancelled, false otherwise
     */
    void setCancelled(boolean cancel);
}
