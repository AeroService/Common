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

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class CachedTaskExecutor implements TaskExecutor {

    private final ExecutorService executorService;
    private final ScheduledExecutorService timerExecutionService;

    private CachedTaskExecutor() {
        this.executorService = Executors.newCachedThreadPool(this::createThread);
        this.timerExecutionService = Executors.newSingleThreadScheduledExecutor(this::createTimerThread);
    }

    public static @NotNull TaskExecutor create() {
        return new CachedTaskExecutor();
    }

    private Thread createThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setName("Task Chain - #" + thread.getId());
        thread.setDaemon(true);
        return thread;
    }

    private Thread createTimerThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setName("Task Chain Timer");
        thread.setDaemon(true);
        return thread;
    }

    @Override
    public boolean isMainThread() {
        return Thread.currentThread().getId() == 1;
    }

    @Override
    public @NotNull Task executeInMain(@NotNull Runnable runnable) {
        return this.execute(new TaskImpl.RunnableTaskImpl(runnable));
    }

    @Override
    public @NotNull Task executeAsync(@NotNull Runnable runnable) {
        return this.execute(new TaskImpl.FutureTaskImpl(() -> this.executorService.submit(runnable)));
    }

    @Override
    public @NotNull Task executeWithDelay(@NotNull Runnable runnable, long delay, @NotNull TimeUnit timeUnit) {
        return this.execute(new TaskImpl.FutureTaskImpl(() -> this.timerExecutionService.schedule(runnable, delay, timeUnit)));
    }

    @Override
    public @NotNull Task executeInRepeat(@NotNull Runnable runnable, long initialDelay, long delay, @NotNull TimeUnit timeUnit) {
        return this.execute(new TaskImpl.FutureTaskImpl(() -> this.timerExecutionService.scheduleAtFixedRate(runnable, initialDelay, delay, timeUnit)));
    }

    private @NotNull Task execute(TaskImpl.@NotNull AbstractTask task) {
        task.run();
        return task;
    }

    @Override
    public boolean isShutdown() {
        return this.executorService.isShutdown() || this.timerExecutionService.isShutdown();
    }

    @Override
    public boolean shutdown() {
        try {
            this.timerExecutionService.shutdownNow();
            this.executorService.shutdown();
            return this.executorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
