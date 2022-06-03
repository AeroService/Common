package de.natrox.common.counter;

import de.natrox.common.scheduler.Scheduler;
import de.natrox.common.validate.Check;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

final class CounterBuilderImpl implements Counter.Builder {

    private final Scheduler scheduler;

    private long startCount;
    private long stopCount;
    private long tick;
    private TimeUnit tickUnit;

    private Consumer<Counter> startHandler;
    private Consumer<Counter> tickHandler;
    private Consumer<Counter> finishHandler;
    private Consumer<Counter> cancelHandler;

    CounterBuilderImpl(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public Counter.@NotNull Builder startCount(long startCount) {
        this.startCount = startCount;
        return this;
    }

    @Override
    public Counter.@NotNull Builder stopCount(long stopCount) {
        this.stopCount = stopCount;
        return this;
    }

    @Override
    public Counter.@NotNull Builder tick(long tick, @NotNull TimeUnit tickUnit) {
        Check.notNull(tickUnit, "tickUnit");
        Check.argCondition(tick <= 0, "tick must be positive");
        this.tick = tick;
        this.tickUnit = tickUnit;
        return this;
    }

    @Override
    public Counter.@NotNull Builder startHandler(@Nullable Consumer<Counter> startHandler) {
        this.startHandler = startHandler;
        return this;
    }

    @Override
    public Counter.@NotNull Builder tickHandler(@Nullable Consumer<Counter> tickHandler) {
        this.tickHandler = tickHandler;
        return this;
    }

    @Override
    public Counter.@NotNull Builder finishHandler(@Nullable Consumer<Counter> finishHandler) {
        this.finishHandler = finishHandler;
        return this;
    }

    @Override
    public Counter.@NotNull Builder cancelHandler(@Nullable Consumer<Counter> cancelHandler) {
        this.cancelHandler = cancelHandler;
        return this;
    }

    @Override
    public @NotNull Counter build() {
        return new CounterImpl(this.scheduler, this.startCount, this.stopCount, this.tick, this.tickUnit, this.startHandler, this.tickHandler, this.finishHandler, this.cancelHandler);
    }
}
