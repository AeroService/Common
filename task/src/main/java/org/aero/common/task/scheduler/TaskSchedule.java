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
import java.time.temporal.TemporalUnit;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("MissingJavaDocType")
public sealed interface TaskSchedule permits TaskScheduleImpl.DurationSchedule, TaskScheduleImpl.FutureSchedule,
    TaskScheduleImpl.Immediate, TaskScheduleImpl.Stop {

    @SuppressWarnings("MissingJavadocMethod")
    static @NotNull TaskSchedule future(@NotNull final CompletableFuture<?> future) {
        Check.notNull(future, "future");
        return new TaskScheduleImpl.FutureSchedule(future);
    }

    @SuppressWarnings("MissingJavadocMethod")
    static @NotNull TaskSchedule stop() {
        return TaskScheduleImpl.STOP;
    }

    @SuppressWarnings("MissingJavadocMethod")
    static @NotNull TaskSchedule immediate() {
        return TaskScheduleImpl.IMMEDIATE;
    }

    @SuppressWarnings("MissingJavadocMethod")
    static @NotNull TaskSchedule duration(@NotNull final Duration duration) {
        Check.notNull(duration, "duration");
        return new TaskScheduleImpl.DurationSchedule(duration);
    }

    @SuppressWarnings("MissingJavadocMethod")
    static @NotNull TaskSchedule duration(final long amount, @NotNull final TemporalUnit unit) {
        Check.notNull(unit, "unit");
        return TaskSchedule.duration(Duration.of(amount, unit));
    }

    @SuppressWarnings("MissingJavadocMethod")
    static @NotNull TaskSchedule hours(final long hours) {
        return TaskSchedule.duration(Duration.ofHours(hours));
    }

    @SuppressWarnings("MissingJavadocMethod")
    static @NotNull TaskSchedule minutes(final long minutes) {
        return TaskSchedule.duration(Duration.ofMinutes(minutes));
    }

    @SuppressWarnings("MissingJavadocMethod")
    static @NotNull TaskSchedule seconds(final long seconds) {
        return TaskSchedule.duration(Duration.ofSeconds(seconds));
    }

    @SuppressWarnings("MissingJavadocMethod")
    static @NotNull TaskSchedule millis(final long millis) {
        return TaskSchedule.duration(Duration.ofMillis(millis));
    }
}
