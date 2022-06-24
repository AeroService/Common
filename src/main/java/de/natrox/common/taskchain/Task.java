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

import java.util.concurrent.CompletableFuture;

/**
 * Represents a task that can be executed on any thread.
 */
public interface Task extends Runnable {

    @Override
    void run();

    /**
     * Represents a task that does not return immediately. A supplied Consumer controls when
     * the chain should proceed to the next task. This is a Callback style API
     * in relation to the Future based API.
     */
    interface CallbackTask extends Task {

        @Override
        default void run() {
            this.run(() -> {

            });
        }

        void run(@NotNull Runnable callback);
    }

    /**
     * Represents a task that returns a future to be completed later.
     */
    interface FutureTask extends Task {

        @Override
        default void run() {
            CompletableFuture<?> future = this.runFuture();
        }

        @NotNull CompletableFuture<?> runFuture();

    }

}
