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
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;

final class TaskScheduleImpl {

    static TaskSchedule STOP = new Stop();
    static TaskSchedule IMMEDIATE = new Immediate();

    record DurationSchedule(@NotNull Duration duration) implements TaskSchedule {

    }

    record FutureSchedule(CompletableFuture<?> future) implements TaskSchedule {
    }

    record Stop() implements TaskSchedule {

    }

    record Immediate() implements TaskSchedule {

    }
}
