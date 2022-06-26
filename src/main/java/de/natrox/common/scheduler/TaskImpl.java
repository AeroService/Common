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

package de.natrox.common.scheduler;

import de.natrox.common.validate.Check;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;

final class TaskImpl implements Runnable, Task {

    private final SchedulerImpl scheduler;
    private final Runnable runnable;
    private final Runnable doneCallback;
    private final long delay;
    private final long repeat;
    private @Nullable de.natrox.common.task.Task task;
    private volatile @Nullable Thread currentTaskThread;

    TaskImpl(SchedulerImpl scheduler, Runnable runnable, Runnable doneCallback, long delay, long repeat) {
        this.scheduler = scheduler;
        this.runnable = runnable;
        this.doneCallback = doneCallback;
        this.delay = delay;
        this.repeat = repeat;
    }

    void schedule() {
        if (this.repeat == 0) {
            this.task = this.scheduler
                .taskExecutor()
                .executeWithDelay(this, this.delay, TimeUnit.MILLISECONDS);
        } else {
            this.task = this.scheduler
                .taskExecutor()
                .executeInRepeat(this, this.delay, this.repeat, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public @NotNull Scheduler owner() {
        return this.scheduler;
    }

    @Override
    public @NotNull TaskStatus status() {
        if (this.task == null) {
            return TaskStatus.SCHEDULED;
        }

        if (this.task.isCancelled()) {
            return TaskStatus.CANCELLED;
        }

        if (this.task.isDone()) {
            return TaskStatus.FINISHED;
        }

        return TaskStatus.SCHEDULED;
    }

    @Override
    public void cancel() {
        if (this.task == null)
            return;

        this.task.cancel();

        Thread cur = this.currentTaskThread;
        if (cur != null) {
            cur.interrupt();
        }

        this.done();
    }

    @Override
    public void run() {
        this.scheduler.taskExecutor().executeAsync(this::execute);
    }

    private void execute() {
        this.currentTaskThread = Thread.currentThread();
        this.runnable.run();
        this.currentTaskThread = null;
        this.done();
    }

    private void done() {
        this.doneCallback.run();
    }

    static final class BuilderImpl implements Task.Builder {

        private final SchedulerImpl scheduler;
        private final Runnable runnable;
        private long delay; // ms
        private long repeat; // ms

        BuilderImpl(SchedulerImpl scheduler, Runnable runnable) {
            this.scheduler = scheduler;
            this.runnable = runnable;
        }

        @Override
        public Task.@NotNull Builder delay(long time, @NotNull TimeUnit timeUnit) {
            Check.notNull(timeUnit, "timeUnit");
            this.delay = timeUnit.toMillis(time);
            return this;
        }

        @Override
        public Task.@NotNull Builder repeat(long time, @NotNull TimeUnit timeUnit) {
            Check.notNull(timeUnit, "timeUnit");
            this.repeat = timeUnit.toMillis(time);
            return this;
        }

        @Override
        public Task.@NotNull Builder clearDelay() {
            this.delay = 0;
            return this;
        }

        @Override
        public Task.@NotNull Builder clearRepeat() {
            this.repeat = 0;
            return this;
        }

        @Override
        public @NotNull Task schedule(@Nullable Runnable doneCallback) {
            TaskImpl task = new TaskImpl(this.scheduler, this.runnable, doneCallback, this.delay, this.repeat);
            task.schedule();
            return task;
        }
    }

}
