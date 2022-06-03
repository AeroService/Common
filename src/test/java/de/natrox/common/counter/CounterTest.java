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
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

public class CounterTest {

    @Test
    public void startTest() {
        Counter countdown = Counter.builder()
            .tick(100, ChronoUnit.MILLIS)
            .startCount(5)
            .stopCount(1)
            .build();
        assertFalse(countdown.isRunning(), "The countdown has not yet started");
        countdown.start();
        assertTrue(countdown.isRunning(), "The countdown has already started");
        countdown.stop();
    }

    @Test
    public void pauseTest() {
        Counter countdown = Counter.builder()
            .tick(100, ChronoUnit.MILLIS)
            .startCount(5)
            .stopCount(1)
            .build();
        assertFalse(countdown.isPaused(), "The counter is not paused");
        countdown.start();
        assertFalse(countdown.isPaused(), "The counter is not paused");
        countdown.pause();
        assertTrue(countdown.isPaused(), "The counter is currently paused");
        assertFalse(countdown.isRunning(), "The counter is currently paused");
        countdown.resume();
        assertFalse(countdown.isPaused(), "The counter is now resumed");
        assertTrue(countdown.isRunning(), "The counter is now resumed");
    }

    @Test
    public void stopTest() {
        Counter countdown = Counter.builder()
            .tick(100, ChronoUnit.MILLIS)
            .startCount(5)
            .stopCount(1)
            .build();
        countdown.start();
        assertTrue(countdown.isRunning(), "The counter has already started");
        countdown.stop();
        assertFalse(countdown.isRunning(), "The counter is now stopped");
    }

    @Test
    public void handlerTest() throws InterruptedException {
        AtomicBoolean started = new AtomicBoolean();
        AtomicInteger ticks = new AtomicInteger();
        AtomicBoolean finished = new AtomicBoolean();
        AtomicBoolean canceled = new AtomicBoolean();
        Counter countdown = Counter.builder()
                    .tick(100, ChronoUnit.MILLIS)
                    .startCount(5)
                    .stopCount(1)
                    .startHandler(counter -> started.set(true))
                    .tickHandler(counter -> ticks.incrementAndGet())
                    .finishHandler(counter -> finished.set(true))
                    .cancelHandler(counter -> canceled.set(true))
                    .build();

        assertEquals(ticks.get(), 0, "The counter has not yet ticked");

        assertFalse(started.get(), "The counter has not yet started");
        countdown.start();
        assertTrue(started.get(), "The counter has now started");

        Thread.sleep(1500);
        assertEquals(6, ticks.get(), "The counter has already ticked exactly 6 times");
        assertTrue(finished.get());

        assertFalse(canceled.get(), "The counter was yet not canceled");
        countdown.stop();
        assertFalse(canceled.get(), "The counter was yet not canceled");

        countdown.start();
        Thread.sleep(1000);
        assertTrue(finished.get(), "The counter has finished yet");
        finished.set(false);
        countdown.start();
        Thread.sleep(50);
        assertFalse(finished.get(), "The counter has not finished yet");
        Thread.sleep(950);
        assertTrue(finished.get(), "The counter is now finished");
    }

    @Test
    public void tickTest() throws InterruptedException{
        AtomicLong upTicked = new AtomicLong();
        AtomicLong downTicked = new AtomicLong();
        Counter upTicker = Counter.builder()
            .tick(100, ChronoUnit.MILLIS)
            .startCount(5)
            .stopCount(1)
            .tickHandler(counter -> upTicked.incrementAndGet())
            .build();
        Counter downTicker = Counter.builder()
            .tick(100, ChronoUnit.MILLIS)
            .startCount(1)
            .stopCount(5)
            .tickHandler(counter -> downTicked.incrementAndGet())
            .build();
        upTicker.start();
        Thread.sleep(1000);
        downTicker.start();
        Thread.sleep(1000);
        assertEquals(upTicked.get(), downTicked.get(), "Number of executed Ticks not equal to expected");
        assertEquals(downTicked.get(), 6, "Number of executed Ticks not equal to expected");
    }
}
