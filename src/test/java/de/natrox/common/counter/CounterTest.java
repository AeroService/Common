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
import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class CounterTest {

    @Test
    public void startTest() {
        Countdown countdown = Counter.builder()
            .tick(100, ChronoUnit.MILLIS)
            .startCount(5)
            .stopCount(1)
            .buildCountdown();
        assertFalse(countdown.isRunning(), "The countdown has not yet started");
        countdown.start();
        assertTrue(countdown.isRunning(), "The countdown has already started");
        countdown.stop();
    }

    @Test
    public void pauseTest() {
        Countdown countdown = Counter.builder()
            .tick(100, ChronoUnit.MILLIS)
            .startCount(5)
            .stopCount(1)
            .buildCountdown();
        assertFalse(countdown.isPaused(), "The countdown is not paused");
        countdown.start();
        assertFalse(countdown.isPaused(), "The countdown is not paused");
        countdown.pause();
        assertTrue(countdown.isPaused(), "The countdown is currently paused");
        assertFalse(countdown.isRunning(), "The countdown is currently paused");
        countdown.resume();
        assertFalse(countdown.isPaused(), "The countdown is now resumed");
        assertTrue(countdown.isRunning(), "The countdown is now resumed");
    }

    @Test
    public void stopTest() {
        Countdown countdown = Counter.builder()
            .tick(100, ChronoUnit.MILLIS)
            .startCount(5)
            .stopCount(1)
            .buildCountdown();
        countdown.start();
        assertTrue(countdown.isRunning(), "The countdown has already started");
        countdown.stop();
        assertFalse(countdown.isRunning(), "The countdown is now stopped");
    }

    @Test
    public void handlerTest() throws InterruptedException {
        AtomicBoolean started = new AtomicBoolean();
        AtomicInteger ticks = new AtomicInteger();
        AtomicBoolean finished = new AtomicBoolean();
        AtomicBoolean canceled = new AtomicBoolean();
        Countdown countdown = Counter.builder()
                    .tick(100, ChronoUnit.MILLIS)
                    .startCount(5)
                    .stopCount(1)
                    .startHandler(counter -> started.set(true))
                    .tickHandler(counter -> ticks.incrementAndGet())
                    .finishHandler(counter -> finished.set(true))
                    .cancelHandler(counter -> canceled.set(true))
                    .buildCountdown();

        assertEquals(ticks.get(), 0, "The countdown has not yet ticked");

        assertFalse(started.get(), "The countdown has not yet started");
        countdown.start();
        assertTrue(started.get(), "The countdown has now started");

        Thread.sleep(1500);
        assertEquals(6, ticks.get(), "The countdown has already ticked exactly 6 times");
        assertTrue(finished.get());

        assertFalse(canceled.get(), "The countdown was yet not canceled");
        countdown.stop();
        assertFalse(canceled.get(), "The countdown was yet not canceled");

        countdown.start();
        Thread.sleep(1000);
        assertTrue(finished.get(), "The countdown has finished yet");
        finished.set(false);
        countdown.start();
        Thread.sleep(50);
        assertFalse(finished.get(), "The countdown has not finished yet");
        Thread.sleep(950);
        assertTrue(finished.get(), "The countdown is now finished");
    }
}
