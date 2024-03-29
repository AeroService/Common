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

import org.aero.common.core.validate.Check;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

final class TaskImpl implements Task {

    private final SchedulerImpl scheduler;
    private final Supplier<TaskSchedule> task;

    private volatile boolean alive = true;
    private volatile boolean done = false;

    TaskImpl(final SchedulerImpl scheduler, final Supplier<TaskSchedule> task) {
        this.scheduler = scheduler;
        this.task = task;
    }

    @Override
    public @NotNull Scheduler owner() {
        return this.scheduler;
    }

    @Override
    public @NotNull TaskStatus status() {
        if (this.done) {
            return TaskStatus.FINISHED;
        }

        if (!this.alive) {
            return TaskStatus.CANCELLED;
        }

        return TaskStatus.SCHEDULED;
    }

    @Override
    public boolean isAlive() {
        return this.alive;
    }

    @Override
    public void cancel() {
        this.alive = false;
    }

    public void done() {
        this.done = true;
        this.cancel();
    }

    public Supplier<TaskSchedule> task() {
        return this.task;
    }

    static final class BuilderImpl implements Task.Builder {

        private final Scheduler scheduler;
        private final Runnable runnable;
        private TaskSchedule delay = TaskSchedule.immediate();
        private TaskSchedule repeat = TaskSchedule.stop();

        BuilderImpl(final Scheduler scheduler, final Runnable runnable) {
            this.scheduler = scheduler;
            this.runnable = runnable;
        }

        @Override
        public Task.@NotNull Builder delay(@NotNull final TaskSchedule schedule) {
            Check.notNull(schedule, "schedule");
            this.delay = schedule;
            return this;
        }

        @Override
        public Task.@NotNull Builder repeat(@NotNull final TaskSchedule schedule) {
            Check.notNull(schedule, "schedule");
            this.repeat = schedule;
            return this;
        }

        @Override
        public @NotNull Task schedule(final Runnable doneCallback) {
            final Runnable runnable = this.runnable;
            final TaskSchedule delay = this.delay;
            final TaskSchedule repeat = this.repeat;
            return this.scheduler.submitTask(new Supplier<>() {
                boolean first = true;

                @Override
                public TaskSchedule get() {
                    if (this.first) {
                        this.first = false;
                        return delay;
                    }
                    runnable.run();
                    return repeat;
                }
            });
        }
    }
}
