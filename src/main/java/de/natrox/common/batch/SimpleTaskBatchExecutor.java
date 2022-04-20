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

package de.natrox.common.batch;

import com.google.common.base.Preconditions;
import de.natrox.common.runnable.CatchingRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

final class SimpleTaskBatchExecutor implements TaskBatchExecutor {

    private final ExecutorService executor;

    protected SimpleTaskBatchExecutor() {
        this.executor = Executors.newSingleThreadExecutor(runnable -> {
            var thread = new Thread(runnable);
            thread.setName("Task Batch");
            return thread;
        });
    }

    @Override
    public void async(@NotNull Runnable runnable) {
        Preconditions.checkNotNull(runnable, "runnable");
        this.executor.submit(new CatchingRunnable(runnable));
    }

    @Override
    public void sync(@NotNull Runnable runnable) {
        Preconditions.checkNotNull(runnable, "runnable");
        runnable.run();
    }

    @Override
    public List<Runnable> interrupt() {
        return this.executor.shutdownNow();
    }

    @Override
    public void shutdown() {
        this.executor.shutdown();
    }
}
