/*
 * Copyright 2020-2022 Conelux
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

package org.conelux.common.task.scheduler;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import org.conelux.common.core.validate.Check;
import org.jetbrains.annotations.NotNull;

final class SchedulerImpl implements Scheduler {

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        return thread;
    });

    @Override
    public @NotNull Task submitTask(@NotNull Supplier<TaskSchedule> task) {
        Check.notNull(task, "task");

        TaskImpl taskWrapper = new TaskImpl(this, task);
        this.handleTask(taskWrapper);
        return taskWrapper;
    }

    private void execute(TaskImpl task) {
        if (!task.isAlive()) {
            return;
        }

        this.executor.submit(() -> this.handleTask(task));
    }

    private void handleTask(TaskImpl task) {
        TaskSchedule schedule = task.task().get();

        if (schedule instanceof TaskScheduleImpl.DurationSchedule durationSchedule) {
            Duration duration = durationSchedule.duration();

            this.executor.schedule(() -> this.execute(task), duration.toMillis(), TimeUnit.MILLISECONDS);
        } else if (schedule instanceof TaskScheduleImpl.FutureSchedule futureSchedule) {
            futureSchedule.future().thenRun(() -> this.execute(task));
        } else if (schedule instanceof TaskScheduleImpl.Immediate) {
            this.execute(task);
        } else if (schedule instanceof TaskScheduleImpl.Stop) {
            task.done();
        }
    }
}
