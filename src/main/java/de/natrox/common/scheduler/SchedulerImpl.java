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

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

final class SchedulerImpl implements Scheduler {

    private final ExecutorService taskService;
    private final ScheduledExecutorService timerExecutionService;
    private final Set<Task> tasks = new LinkedHashSet<>();

    public SchedulerImpl() {
        this.taskService = Executors.newCachedThreadPool(this::createThread);
        this.timerExecutionService = Executors.newSingleThreadScheduledExecutor(this::createTimerThread);
    }

    private Thread createThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setName("Task Scheduler - #" + thread.getId());
        thread.setDaemon(true);
        return thread;
    }

    private Thread createTimerThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setName("Task Scheduler Timer");
        thread.setDaemon(true);
        return thread;
    }

    @Override
    public @NotNull Task.Builder buildTask(@NotNull Runnable runnable) {
        Check.notNull(runnable, "runnable");
        return new TaskImpl.BuilderImpl(this, runnable);
    }

    @Override
    public boolean isShutdown() {
        return this.taskService.isShutdown() || this.timerExecutionService.isShutdown();
    }

    public boolean shutdown() throws InterruptedException {
        Collection<Task> terminating;
        synchronized (this.tasks) {
            terminating = Collections.unmodifiableSet(this.tasks);
        }
        for (Task task : terminating) {
            task.cancel();
        }
        this.timerExecutionService.shutdown();
        this.taskService.shutdown();
        return this.taskService.awaitTermination(10, TimeUnit.SECONDS);
    }

    public ExecutorService taskService() {
        return this.taskService;
    }

    public ScheduledExecutorService timerExecutionService() {
        return this.timerExecutionService;
    }

    public Set<Task> tasks() {
        return this.tasks;
    }
}
