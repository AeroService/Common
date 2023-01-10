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

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;

public sealed interface TaskSchedule permits TaskScheduleImpl.DurationSchedule, TaskScheduleImpl.FutureSchedule,
    TaskScheduleImpl.Immediate, TaskScheduleImpl.Stop {

    static @NotNull TaskSchedule duration(@NotNull Duration duration) {
        return new TaskScheduleImpl.DurationSchedule(duration);
    }

    static @NotNull TaskSchedule future(@NotNull CompletableFuture<?> future) {
        return new TaskScheduleImpl.FutureSchedule(future);
    }

    static @NotNull TaskSchedule stop() {
        return TaskScheduleImpl.STOP;
    }

    static @NotNull TaskSchedule immediate() {
        return TaskScheduleImpl.IMMEDIATE;
    }

    static @NotNull TaskSchedule duration(long amount, @NotNull TemporalUnit unit) {
        return TaskSchedule.duration(Duration.of(amount, unit));
    }

    static @NotNull TaskSchedule hours(long hours) {
        return TaskSchedule.duration(Duration.ofHours(hours));
    }

    static @NotNull TaskSchedule minutes(long minutes) {
        return TaskSchedule.duration(Duration.ofMinutes(minutes));
    }

    static @NotNull TaskSchedule seconds(long seconds) {
        return TaskSchedule.duration(Duration.ofSeconds(seconds));
    }

    static @NotNull TaskSchedule millis(long millis) {
        return TaskSchedule.duration(Duration.ofMillis(millis));
    }
}
