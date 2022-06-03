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

import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public sealed interface Counter permits Countdown, Timer {

    static Counter.Builder builder() {
        return new CounterBuilderImpl();
    }

    /**
     * Starts the {@link Counter} if its {@link CounterStatus} is IDLING
     * Sets the {@link CounterStatus} to RUNNING
     */
    void start();

    /**
     * Pauses the {@link Counter} if its {@link CounterStatus} is RUNNING
     * Sets the {@link CounterStatus} to PAUSED
     */
    void pause();

    /**
     * Resumes the {@link Counter} if its {@link CounterStatus} is PAUSED
     * Sets the {@link CounterStatus} to RUNNING
     */
    void resume();


    /**
     * Stops the {@link Counter} if its {@link CounterStatus} not IDLING
     * Sets the {@link CounterStatus} to IDLING
     */
    void stop();

    /**
     * @return true if, and only if the {@link Counter} its {@link CounterStatus} is PAUSED
     */
    boolean isPaused();

    /**
     * @return true if, and only if the {@link Counter} its {@link CounterStatus} is RUNNING
     */
    boolean isRunning();


    /**
     * @return the current {@link CounterStatus}
     */
    CounterStatus status();

    /**
     * @return the number with witch the {@link Counter} starts
     */
    long startCount();

    /**
     * @return the number with witch the {@link Counter} stops
     */
    long stopCount();

    /**
     * @return the value of a single tick
     */
    long tickValue();

    /**
     * @return the amount of counted numbers after start
     */
    long tickedTime();

    /**
     * @return the amount of counted numbers after start
     */
    long currentCount();


    /**
     *
     * Sets the current number the specified value
     *
     * @param currentCount the new current number
     */
    void currentCount(long currentCount);

    /**
     * @return the {@link TimeUnit} to multiply with the tickValue to get the delay between two ticks
     */
    TimeUnit tickUnit();

    sealed interface Builder permits CounterBuilderImpl {

        /**
         *
         * Sets the startCount to the specified value
         *
         * @param startCount the startCount
         * @return the {@link Builder}
         */
        Builder startCount(long startCount);

        /**
         *
         * Sets the stopCount to the specified value
         *
         * @param stopCount the stopCount
         * @return the {@link Builder}
         */
        Builder stopCount(long stopCount);


        /**
         *
         * Sets the scheduler
         *
         * @param scheduler the scheduler
         * @return the {@link Builder}
         */
        Builder scheduler(@NotNull Scheduler scheduler);

        /**
         *
         * Sets the delay between two ticks
         *
         * @param tick the amount of tickUnits needed to the next tick
         * @param tickUnit the matching type to the tick parameter
         * @return the {@link Builder}
         */
        Builder tick(long tick, @NotNull TimeUnit tickUnit);

        /**
         *
         * Sets the delay between two ticks
         *
         * @param tick the amount of tickUnits needed to the next tick
         * @param tickUnit the matching type to the tick parameter
         * @return the {@link Builder}
         */
        default Builder tick(long tick, @NotNull ChronoUnit tickUnit) {
            return tick(tick, TimeUnit.of(tickUnit));
        }

        /**
         *
         * Sets the startHandler
         * Gets executed if the {@link Counter} is started
         *
         * @param startHandler a consumer consuming a {@link CounterInfo}
         * @return the {@link Builder}
         */
        Builder startHandler(@NotNull Consumer<CounterInfo> startHandler);

        /**
         *
         * Sets the tickHandler
         * Gets executed if the {@link Counter} ticked
         *
         * @param tickHandler a consumer consuming a {@link CounterInfo}
         * @return the {@link Builder}
         */
        Builder tickHandler(@NotNull Consumer<CounterInfo> tickHandler);

        /**
         *
         * Sets the finishHandler
         * Gets executed if the {@link Counter} finished counting
         *
         * @param finishHandler a consumer consuming a {@link CounterInfo}
         * @return the {@link Builder}
         */
        Builder finishHandler(@NotNull Consumer<CounterInfo> finishHandler);

        /**
         *
         * Sets the cancelHandler
         * Gets executed if the {@link Counter} is canceled
         *
         * @param cancelHandler a consumer consuming a {@link CounterInfo}
         * @return the {@link Builder}
         */
        Builder cancelHandler(@NotNull Consumer<CounterInfo> cancelHandler);

        /**
         *
         * Builds the {@link Counter} as a {@link Countdown}
         * A {@link Countdown} decreases the number by 1 every tick
         *
         * @return the {@link Countdown}
         */
        Countdown buildCountdown();
        /**
         *
         * Builds the {@link Counter} as a {@link Timer}
         * A {@link Timer} increases the number by 1 every tick
         *
         * @return the {@link Timer}
         */
        Timer buildTimer();
    }
}
