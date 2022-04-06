package de.natrox.common.counter;

import de.natrox.common.scheduler.Scheduler;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public abstract class Timer extends Countdown {

    public Timer(@NotNull Scheduler scheduler, int startTime, int stopTime, int tick, TimeUnit timeUnit) {
        super(scheduler, stopTime, startTime, tick, timeUnit);
    }

    @Override
    public int currentTime() {
        return (stopTime - tickedTime()) + startTime;
    }
}
