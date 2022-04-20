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

import de.natrox.common.counter.Countdown;
import de.natrox.common.counter.Timer;
import de.natrox.common.scheduler.Scheduler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class CounterTest {

    public static void main(String[] args) {
        Scheduler scheduler = Scheduler.create();

        var countDown = new TestCountDown(scheduler);
        //countDown.start();

        var timer = new TestTimer(scheduler);
        timer.start();
    }

    static class TestCountDown extends Countdown {

        private final static Logger LOGGER = LoggerFactory.getLogger(TestCountDown.class);

        public TestCountDown(@NotNull Scheduler scheduler) {
            super(scheduler, 60, 1, 1, TimeUnit.SECONDS);
        }

        @Override
        protected void handleStart() {
            LOGGER.info("Countdown started");
        }

        @Override
        protected void handleTick() {
            LOGGER.info("Countdown tick -> " + currentTime());
        }

        @Override
        protected void handleFinish() {
            LOGGER.info("Countdown finished");
        }

        @Override
        protected void handleCancel() {
            LOGGER.info("Countdown canceled");
        }

    }

    static class TestTimer extends Timer {

        private final static Logger LOGGER = LoggerFactory.getLogger(TestTimer.class);

        public TestTimer(@NotNull Scheduler scheduler) {
            super(scheduler, 1, 60, 1, TimeUnit.SECONDS);
        }

        @Override
        protected void handleStart() {
            LOGGER.info("Timer started");
        }

        @Override
        protected void handleTick() {
            LOGGER.info("Timer tick -> " + currentTime());
        }

        @Override
        protected void handleFinish() {
            LOGGER.info("Timer finish");
        }

        @Override
        protected void handleCancel() {
            LOGGER.info("Timer cancel");
        }
    }

}
