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

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

final class SchedulerImpl implements Scheduler {

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(r -> {
        final Thread thread = new Thread(r);
        thread.setDaemon(true);
        return thread;
    });

    SchedulerImpl() {

    }

    @Override
    public @NotNull Task submitTask(@NotNull final Supplier<TaskSchedule> task) {
        Check.notNull(task, "task");

        final TaskImpl taskWrapper = new TaskImpl(this, task);
        this.handleTask(taskWrapper);
        return taskWrapper;
    }

    private void execute(final TaskImpl task) {
        if (!task.isAlive()) {
            return;
        }

        this.executor.submit(() -> this.handleTask(task));
    }

    private void handleTask(final TaskImpl task) {
        final TaskSchedule schedule = task.task().get();

        if (schedule instanceof final TaskScheduleImpl.DurationSchedule durationSchedule) {
            final Duration duration = durationSchedule.duration();

            this.executor.schedule(() -> this.execute(task), duration.toMillis(), TimeUnit.MILLISECONDS);
        } else if (schedule instanceof final TaskScheduleImpl.FutureSchedule futureSchedule) {
            futureSchedule.future().thenRun(() -> this.execute(task));
        } else if (schedule instanceof TaskScheduleImpl.Immediate) {
            this.execute(task);
        } else if (schedule instanceof TaskScheduleImpl.Stop) {
            task.done();
        }
    }
}
