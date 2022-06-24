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
    public @NotNull Task executeInMain(@NotNull Runnable runnable) {
        AbstractTask task = new AbstractTask() {
            @Override
            public void run() {
                runnable.run();
            }

            @Override
            public void cancel() {

            }
        };
        task.run();

        return task;
    }

    @Override
    public @NotNull Task executeAsync(@NotNull Runnable runnable) {
        AbstractTask task = new AbstractTask.SchedulerTask() {
            @Override
            de.natrox.common.scheduler.Task runScheduler() {
                return scheduler.buildTask(runnable).schedule();
            }
        };
        task.run();

        return task;
    }

    @Override
    public @NotNull Task executeWithDelay(@NotNull Runnable runnable, long delay, @NotNull TimeUnit timeUnit) {
        AbstractTask task = new AbstractTask.SchedulerTask() {
            @Override
            de.natrox.common.scheduler.Task runScheduler() {
                return scheduler.buildTask(runnable).delay(delay, timeUnit).schedule();
            }
        };
        task.run();

        return task;
    }

    @Override
    public @NotNull Task executeInRepeat(@NotNull Runnable runnable, long delay, @NotNull TimeUnit timeUnit) {
        AbstractTask task = new AbstractTask.SchedulerTask() {
            @Override
            de.natrox.common.scheduler.Task runScheduler() {
                return scheduler.buildTask(runnable).repeat(delay, timeUnit).schedule();
            }
        };
        task.run();

        return task;
    }

    @Override
    public boolean isShutdown() {
        return this.scheduler.isShutdown();
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
