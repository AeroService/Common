/*
 * Copyright 2020-2022 NatroxMC team
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

package de.natrox.common.counter;import de.natrox.common.counter.Countdown;
import de.natrox.common.scheduler.Scheduler;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CounterTest {

    @Test
    public void startTest() {
        var scheduler = Scheduler.create();
        var countDown = new TestCountDown(scheduler);
        assertFalse(countDown.isRunning(), "The countdown has not yet started");
        countDown.start();
        assertTrue(countDown.isRunning(), "The countdown has already started");
        countDown.stop();
    }

    @Test
    public void pauseTest() {
        var scheduler = Scheduler.create();
        var countDown = new TestCountDown(scheduler);

        assertFalse(countDown.isPaused(), "The countdown is not paused");
        countDown.start();
        assertFalse(countDown.isPaused(), "The countdown is not paused");
        countDown.pause();
        assertTrue(countDown.isPaused(), "The countdown is currently paused");
        assertFalse(countDown.isRunning(), "The countdown is currently paused");

        countDown.resume();
        assertFalse(countDown.isPaused(), "The countdown is now resumed");
        assertTrue(countDown.isRunning(), "The countdown is now resumed");
    }

    @Test
    public void stopTest() {
        var scheduler = Scheduler.create();
        var countDown = new TestCountDown(scheduler);

        countDown.start();
        assertTrue(countDown.isRunning(), "The countdown has already started");
        countDown.stop();
        assertFalse(countDown.isRunning(), "The countdown is now stopped");
    }

    @Test
    public void handlerTest() throws InterruptedException {
        var scheduler = Scheduler.create();
        var countDown = new TestCountDown(scheduler);

        assertFalse(countDown.started, "The countdown has not yet started");
        countDown.start();
        assertTrue(countDown.started, "The countdown has now started");

        assertFalse(countDown.canceled, "The countdown was yet not canceled");
        countDown.stop();
        assertTrue(countDown.canceled, "The countdown was canceled");

        assertFalse(countDown.finished, "The countdown is not finished yet");
        countDown.start();
        Thread.sleep(600);
        assertFalse(countDown.finished, "400 ms remaining");
        Thread.sleep(600);
        assertTrue(countDown.finished, "The countdown is now finished");
    }


    class TestCountDown extends Countdown {

        public boolean started = false;
        public boolean finished = false;
        public boolean canceled = false;

        public TestCountDown(@NotNull Scheduler scheduler) {
            super(scheduler, 1000, 0, 1, TimeUnit.MILLISECONDS);
        }

        @Override
        protected void handleStart() {
            this.started = true;
        }

        @Override
        protected void handleTick() {

        }

        @Override
        protected void handleFinish() {
            this.finished = true;
        }

        @Override
        protected void handleCancel() {
            this.canceled = true;
        }
    }
}
