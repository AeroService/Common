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

import de.natrox.common.task.TaskExecutor;
import de.natrox.common.validate.Check;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

final class SchedulerImpl implements Scheduler {

    private final TaskExecutor taskExecutor;
    private final Set<Task> tasks = new LinkedHashSet<>();

    SchedulerImpl(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Override
    public @NotNull Task.Builder buildTask(@NotNull Runnable runnable) {
        Check.notNull(runnable, "runnable");
        return new TaskImpl.BuilderImpl(this, runnable);
    }

    @Override
    public boolean isShutdown() {
        return this.taskExecutor.isShutdown();
    }

    public boolean shutdown() {
        Collection<Task> terminating;
        synchronized (this.tasks) {
            terminating = Collections.unmodifiableSet(this.tasks);
        }
        for (Task task : terminating) {
            task.cancel();
        }
        return this.taskExecutor.shutdown();
    }

    TaskExecutor taskExecutor() {
        return this.taskExecutor;
    }

    public Set<Task> tasks() {
        return this.tasks;
    }
}
