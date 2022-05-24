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

package de.natrox.common.batch;

import de.natrox.common.validate.Check;
import de.natrox.common.runnable.CatchingRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

final class SimpleTaskBatch implements TaskBatch {

    private final TaskBatchExecutor executor;
    private final List<TaskInfo> tasks;
    private final AtomicBoolean locked;
    private @Nullable Runnable callback;

    protected SimpleTaskBatch(@NotNull TaskBatchExecutor executor) {
        Check.notNull(executor, "executor");
        this.executor = executor;

        this.tasks = new ArrayList<>();
        this.locked = new AtomicBoolean(false);
    }

    @Override
    public @NotNull SimpleTaskBatch sync(@NotNull Runnable runnable) {
        Check.notNull(runnable, "runnable");
        this.addTask(TaskType.SYNC, runnable, 0);
        return this;
    }

    @Override
    public @NotNull SimpleTaskBatch async(@NotNull Runnable runnable) {
        Check.notNull(runnable, "runnable");
        this.addTask(TaskType.ASYNC, runnable, 0);
        return this;
    }

    @Override
    public @NotNull TaskBatch wait(Duration duration) {
        Check.notNull(duration, "duration");
        this.addTask(TaskType.WAIT, null, duration.toMillis());
        return this;
    }

    @Override
    public @NotNull TaskBatch wait(long duration, @NotNull TemporalUnit temporalUnit) {
        Check.notNull(temporalUnit, "temporalUnit");
        this.addTask(TaskType.WAIT, null, Duration.of(duration, temporalUnit).toMillis());
        return this;
    }

    @Override
    public @NotNull SimpleTaskBatch wait(long delay, @NotNull TimeUnit timeUnit) {
        Check.notNull(timeUnit, "timeUnit");
        this.addTask(TaskType.WAIT, null, timeUnit.toMillis(delay));
        return this;
    }

    @Override
    public void execute(@Nullable Runnable callback) {
        this.callback = callback;
        this.executor.async(new CatchingRunnable(this::runBatch));
    }

    @Override
    public void execute() {
        execute(null);
    }

    @Override
    public @NotNull List<Runnable> interrupt() {
        return executor.interrupt();
    }

    private void runBatch() {
        for (var task : tasks) {
            while (locked.get()) {

            }
            locked.set(true);
            if (task.taskType.equals(TaskType.SYNC))
                executor.sync(task.runnable());
            else if (task.taskType.equals(TaskType.ASYNC))
                executor.async(task.runnable());
            else {
                executor.async(new CatchingRunnable(() -> {
                    try {
                        Thread.sleep(task.delay());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    locked.set(false);
                }));
            }
        }
        if (callback != null)
            callback.run();
        executor.shutdown();
    }

    private void addTask(@NotNull TaskType taskType, @Nullable Runnable runnable, long delay) {
        Check.notNull(taskType, "taskType");
        tasks.add(new TaskInfo(delay, taskType, () -> {
            if (runnable != null)
                runnable.run();
            locked.set(false);
        }));
    }

    enum TaskType {
        SYNC,
        ASYNC,
        WAIT
    }

    record TaskInfo(long delay, TaskType taskType, Runnable runnable) {
    }
}
