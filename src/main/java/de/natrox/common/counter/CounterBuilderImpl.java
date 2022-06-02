package de.natrox.common.counter;

import de.natrox.common.scheduler.Scheduler;

import java.util.concurrent.TimeUnit;

public final class CounterBuilderImpl implements Counter.Builder {

    private long startTime;
    private long stopTime;
    private Scheduler scheduler;
    private long tick;
    private TimeUnit tickUnit;


    @Override
    public Counter.Builder startTime(long startTime) {
        this.startTime = startTime;
        return this;
    }

    @Override
    public Counter.Builder stopTime(long stopTime) {
        this.stopTime = stopTime;
        return this;
    }

    @Override
    public Counter.Builder scheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
        return this;
    }

    @Override
    public Counter.Builder tick(long tick, TimeUnit tickUnit) {
        this.tick = tick;
        this.tickUnit = tickUnit;
        return this;
    }

    @Override
    public Countdown buildCountdown() {
        return new Countdown(scheduler, startTime, stopTime, tick, tickUnit);
    }

    @Override
    public Timer buildTimer() {
        return new Timer(scheduler, startTime, stopTime, tick, tickUnit);
    }
}
