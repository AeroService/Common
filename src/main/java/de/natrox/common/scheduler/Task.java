package de.natrox.common.scheduler;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.function.Supplier;

public interface Task {

    int id();

    Scheduler owner();

    void cancel();

    boolean isAlive();

    final class Builder {

        private final Scheduler scheduler;
        private final Runnable runnable;
        private TaskSchedule delay = TaskSchedule.immediate();
        private TaskSchedule repeat = TaskSchedule.stop();

        Builder(Scheduler scheduler, Runnable runnable) {
            this.scheduler = scheduler;
            this.runnable = runnable;
        }

        public @NotNull Builder delay(@NotNull TaskSchedule schedule) {
            this.delay = schedule;
            return this;
        }

        public @NotNull Builder repeat(@NotNull TaskSchedule schedule) {
            this.repeat = schedule;
            return this;
        }

        public @NotNull Task schedule() {
            var runnable = this.runnable;
            var delay = this.delay;
            var repeat = this.repeat;
            return scheduler.submitTask(new Supplier<>() {
                boolean first = true;

                @Override
                public TaskSchedule get() {
                    if (first) {
                        first = false;
                        return delay;
                    }
                    runnable.run();
                    return repeat;
                }
            });
        }

        public @NotNull Builder delay(@NotNull Duration duration) {
            return delay(TaskSchedule.duration(duration));
        }

        public @NotNull Builder delay(long time, @NotNull TemporalUnit unit) {
            return delay(Duration.of(time, unit));
        }

        public @NotNull Builder repeat(@NotNull Duration duration) {
            return repeat(TaskSchedule.duration(duration));
        }

        public @NotNull Builder repeat(long time, @NotNull TemporalUnit unit) {
            return repeat(Duration.of(time, unit));
        }
    }

}
