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

import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public sealed interface Counter permits Countdown, Timer {

    static Counter.Builder builder() {
        return new CounterBuilderImpl();
    }

    void start();

    void pause();

    void resume();

    void stop();

    boolean isPaused();

    boolean isRunning();

    CounterStatus status();

    long startCount();

    long stopCount();

    long tickValue();

    long tickedTime();

    long currentTime();

    void currentTime(long currentTime);

    TimeUnit tickUnit();

    sealed interface Builder permits CounterBuilderImpl {

        Builder startCount(long startCount);

        Builder stopCount(long stopCount);

        Builder scheduler(Scheduler scheduler);

        Builder tick(long tick, TimeUnit tickUnit);

        default Builder tick(long tick, ChronoUnit tickUnit) {
            return tick(tick, TimeUnit.of(tickUnit));
        }

        Builder startHandler(Consumer<CounterInfo> startHandler);

        Builder tickHandler(Consumer<CounterInfo> tickHandler);

        Builder finishHandler(Consumer<CounterInfo> finishHandler);

        Builder cancelHandler(Consumer<CounterInfo> cancelHandler);

        Countdown buildCountdown();

        Timer buildTimer();
    }
}
