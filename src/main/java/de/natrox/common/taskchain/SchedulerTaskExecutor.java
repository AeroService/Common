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

import de.natrox.common.scheduler.Scheduler;
import de.natrox.common.validate.Check;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("ClassCanBeRecord")
public final class SchedulerTaskExecutor implements TaskExecutor {

    private final static Scheduler DEFAULT_SCHEDULER = Scheduler.create();
    private final Scheduler scheduler;

    private SchedulerTaskExecutor(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public static @NotNull TaskExecutor create(@NotNull Scheduler scheduler) {
        Check.notNull(scheduler, "scheduler");
        return new SchedulerTaskExecutor(scheduler);
    }

    public static @NotNull TaskExecutor create() {
        return create(DEFAULT_SCHEDULER);
    }

    @Override
    public boolean isMainThread() {
        return Thread.currentThread().getId() == 1;
    }

    @Override
    public void executeInMain(@NotNull Runnable runnable) {
        runnable.run();
    }

    @Override
    public void executeAsync(@NotNull Runnable runnable) {
        this.scheduler.buildTask(runnable).schedule();
    }

    @Override
    public void executeWithDelay(@NotNull Runnable runnable, long delay, @NotNull TimeUnit timeUnit) {
        this.scheduler.buildTask(runnable).delay(delay, timeUnit).schedule();
    }

    @Override
    public void shutdown() {
        try {
            this.scheduler.shutdown();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
