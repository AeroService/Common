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

import java.util.concurrent.TimeUnit;

public sealed interface Counter permits Countdown, Timer {

    void start();

    void pause();

    void resume();

    void stop();

    boolean isPaused();

    boolean isRunning();

    CounterStatus status();

    long startTime();

    long stopTime();

    long tickValue();

    long currentTime();

    void currentTime(long currentTime);

    TimeUnit tickUnit();

    sealed interface Builder permits CounterBuilderImpl {

        Builder startTime(long startTime);

        Builder stopTime(long stopTime);

        Builder scheduler(Scheduler scheduler);

        Builder tick(long tick, TimeUnit tickUnit);

        Countdown buildCountdown();

        Timer buildTimer();
    }
}
