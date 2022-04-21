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

import de.natrox.common.base.Check;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

final class TaskBuilderImpl implements Task.Builder {

    private final SchedulerImpl scheduler;
    private final Runnable runnable;
    private long delay; // ms
    private long repeat; // ms

    protected TaskBuilderImpl(SchedulerImpl scheduler, Runnable runnable) {
        this.scheduler = scheduler;
        this.runnable = runnable;
    }

    @Override
    public @NotNull TaskBuilderImpl delay(long time, @NotNull TimeUnit timeUnit) {
        Check.notNull(timeUnit, "timeUnit");
        this.delay = timeUnit.toMillis(time);
        return this;
    }

    @Override
    public @NotNull TaskBuilderImpl repeat(long time, @NotNull TimeUnit timeUnit) {
        Check.notNull(timeUnit, "timeUnit");
        this.repeat = timeUnit.toMillis(time);
        return this;
    }

    @Override
    public @NotNull TaskBuilderImpl clearDelay() {
        this.delay = 0;
        return this;
    }

    @Override
    public @NotNull TaskBuilderImpl clearRepeat() {
        this.repeat = 0;
        return this;
    }

    @Override
    public @NotNull Task schedule() {
        var task = new TaskImpl(scheduler, runnable, delay, repeat);
        task.schedule();
        return task;
    }
}
