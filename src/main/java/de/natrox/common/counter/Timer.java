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
import java.util.function.Consumer;

final class Timer extends Countdown implements Counter {

    Timer(
        @NotNull Scheduler scheduler,
        long startCount,
        long stopCount,
        long tick,
        @NotNull TimeUnit timeUnit,
        Consumer<CounterInfo> startHandler,
        Consumer<CounterInfo> tickHandler,
        Consumer<CounterInfo> finishHandler,
        Consumer<CounterInfo> cancelHandler
    ) {
        super(scheduler, stopCount, startCount, tick, timeUnit, startHandler, tickHandler, finishHandler, cancelHandler);
    }

    @Override
    public long currentTime() {
        return (this.stopCount - this.tickedTime()) + this.startCount;
    }
}
