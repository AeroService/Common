package de.natrox.common.scheduler;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public interface Task {

    @NotNull
    Scheduler owner();

    @NotNull
    TaskStatus status();

    void cancel();

    interface Builder {

        @NotNull
        Builder delay(long time, TimeUnit unit);

        @NotNull
        default Builder delay(Duration duration) {
            return delay(duration.toMillis(), TimeUnit.MILLISECONDS);
        }

        @NotNull
        Builder repeat(long time, TimeUnit unit);

        @NotNull
        default Builder repeat(Duration duration) {
            return repeat(duration.toMillis(), TimeUnit.MILLISECONDS);
        }

        @NotNull
        Builder clearDelay();

        @NotNull
        Builder clearRepeat();

        @NotNull
        Task schedule();

    }

}
