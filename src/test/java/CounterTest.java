import de.natrox.common.counter.Countdown;
import de.natrox.common.counter.Timer;
import de.natrox.common.logger.LogManager;
import de.natrox.common.logger.Logger;
import de.natrox.common.scheduler.Scheduler;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
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

        private final static Logger LOGGER = LogManager.logger(TestCountDown.class);

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

        private final static Logger LOGGER = LogManager.logger(TestTimer.class);

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
