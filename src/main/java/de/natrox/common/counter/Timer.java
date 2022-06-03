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

package de.natrox.common.counter;

import de.natrox.common.scheduler.Scheduler;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

final class Timer extends Countdown implements Counter {

    Timer(
        @NotNull Scheduler scheduler,
        long startTime,
        long stopTime,
        long tick,
        @NotNull TimeUnit timeUnit,
        Runnable startHandler,
        Runnable tickHandler,
        Runnable finishHandler,
        Runnable cancelHandler
    ) {
        super(scheduler, stopTime, startTime, tick, timeUnit, startHandler, tickHandler, finishHandler, cancelHandler);
    }

    @Override
    public long currentTime() {
        return (this.stopTime - this.tickedTime()) + this.startTime;
    }
}
