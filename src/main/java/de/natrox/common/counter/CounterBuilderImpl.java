package de.natrox.common.counter;

import de.natrox.common.scheduler.Scheduler;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

final class CounterBuilderImpl implements Counter.Builder {

    private Consumer<CounterInfo> startHandler;
    private Consumer<CounterInfo> tickHandler;
    private Consumer<CounterInfo> finishHandler;
    private Consumer<CounterInfo> cancelHandler;

    private long startCount;
    private long stopCount;
    private Scheduler scheduler;
    private long tick;
    private TimeUnit tickUnit;

    CounterBuilderImpl() {
    }

    @Override
    public Counter.Builder startCount(long startCount) {
        this.startCount = startCount;
        return this;
    }

    @Override
    public Counter.Builder stopCount(long stopCount) {
        this.stopCount = stopCount;
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
    public Counter.Builder startHandler(Consumer<CounterInfo> startHandler) {
        this.startHandler = startHandler;
        return this;
    }

    @Override
    public Counter.Builder tickHandler(Consumer<CounterInfo> tickHandler) {
        this.tickHandler = tickHandler;
        return this;
    }

    @Override
    public Counter.Builder finishHandler(Consumer<CounterInfo> finishHandler) {
        this.finishHandler = finishHandler;
        return this;
    }

    @Override
    public Counter.Builder cancelHandler(Consumer<CounterInfo> cancelHandler) {
        this.cancelHandler = cancelHandler;
        return this;
    }

    @Override
    public Countdown buildCountdown() {
        return new Countdown(scheduler, startCount, stopCount, tick, tickUnit, startHandler, tickHandler, finishHandler, cancelHandler);
    }

    @Override
    public Timer buildTimer() {
        return new Timer(scheduler, startCount, stopCount, tick, tickUnit, startHandler, tickHandler, finishHandler, cancelHandler);
    }
}
