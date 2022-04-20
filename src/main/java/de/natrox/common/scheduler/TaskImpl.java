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

package de.natrox.common.scheduler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

final class TaskImpl implements Runnable, Task {

    private final SchedulerImpl scheduler;
    private final Runnable runnable;
    private final long delay;
    private final long repeat;
    private @Nullable ScheduledFuture<?> future;
    private volatile @Nullable Thread currentTaskThread;

    protected TaskImpl(SchedulerImpl scheduler, Runnable runnable, long delay, long repeat) {
        this.scheduler = scheduler;
        this.runnable = runnable;
        this.delay = delay;
        this.repeat = repeat;
    }

    void schedule() {
        if (repeat == 0) {
            this.future = scheduler
                .timerExecutionService()
                .schedule(this, delay, TimeUnit.MILLISECONDS);
        } else {
            this.future = scheduler
                .timerExecutionService()
                .scheduleAtFixedRate(this, delay, repeat, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public @NotNull Scheduler owner() {
        return scheduler;
    }

    @Override
    public @NotNull TaskStatus status() {
        if (future == null) {
            return TaskStatus.SCHEDULED;
        }

        if (future.isCancelled()) {
            return TaskStatus.CANCELLED;
        }

        if (future.isDone()) {
            return TaskStatus.FINISHED;
        }

        return TaskStatus.SCHEDULED;
    }

    @Override
    public void cancel() {
        if (future != null) {
            future.cancel(false);

            Thread cur = currentTaskThread;
            if (cur != null) {
                cur.interrupt();
            }

            //finish
        }
    }

    @Override
    public void run() {
        scheduler.taskService().execute(() -> {
            currentTaskThread = Thread.currentThread();
            try {
                runnable.run();
            } catch (Throwable e) {
                //noinspection ConstantConditions
                if (e instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                } else {
                    //Log
                }
            } finally {
                if (repeat == 0) {
                    //finish
                }
                currentTaskThread = null;
            }
        });
    }

}
