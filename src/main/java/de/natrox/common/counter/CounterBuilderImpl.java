package de.natrox.common.counter;

import de.natrox.common.scheduler.Scheduler;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

final class CounterBuilderImpl implements Counter.Builder {

    private final Scheduler scheduler;

    private Consumer<Counter> startHandler;
    private Consumer<Counter> tickHandler;
    private Consumer<Counter> finishHandler;
    private Consumer<Counter> cancelHandler;

    private long startCount;
    private long stopCount;
    private long tick;
    private TimeUnit tickUnit;

    CounterBuilderImpl(Scheduler scheduler) {
        this.scheduler = scheduler;
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
    public Counter.Builder tick(long tick, @NotNull TimeUnit tickUnit) {
        this.tick = tick;
        this.tickUnit = tickUnit;
        return this;
    }

    @Override
    public Counter.Builder startHandler(@NotNull Consumer<Counter> startHandler) {
        this.startHandler = startHandler;
        return this;
    }

    @Override
    public Counter.Builder tickHandler(@NotNull Consumer<Counter> tickHandler) {
        this.tickHandler = tickHandler;
        return this;
    }

    @Override
    public Counter.Builder finishHandler(@NotNull Consumer<Counter> finishHandler) {
        this.finishHandler = finishHandler;
        return this;
    }

    @Override
    public Counter.Builder cancelHandler(@NotNull Consumer<Counter> cancelHandler) {
        this.cancelHandler = cancelHandler;
        return this;
    }

    @Override
    public Countdown buildCountdown() {
        return new Countdown(this.scheduler, this.startCount, this.stopCount, this.tick, this.tickUnit, this.startHandler, this.tickHandler, this.finishHandler, this.cancelHandler);
    }

    @Override
    public Timer buildTimer() {
        return new Timer(this.scheduler, this.startCount, this.stopCount, this.tick, this.tickUnit, this.startHandler, this.tickHandler, this.finishHandler, this.cancelHandler);
    }
}
