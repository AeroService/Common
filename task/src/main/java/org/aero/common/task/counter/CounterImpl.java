/*
 * Copyright 2020-2023 AeroService
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

package org.aero.common.task.counter;

import org.aero.common.core.runnable.CatchingRunnable;
import org.aero.common.core.validate.Check;
import org.aero.common.task.count.CountingRunnable;
import org.aero.common.task.scheduler.Scheduler;
import org.aero.common.task.scheduler.Task;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

final class CounterImpl implements Counter {

    private final Scheduler scheduler;
    private final long startCount;
    private final long stopCount;
    private final long tick;
    private final int step;
    private final TimeUnit tickUnit;

    private final Consumer<Counter> startHandler;
    private final Consumer<Counter> tickHandler;
    private final Consumer<Counter> finishHandler;
    private final Consumer<Counter> cancelHandler;

    private volatile CountingRunnable runnable;
    private volatile Task task;
    private volatile CounterStatus status;

    CounterImpl(final BuilderImpl builder) {
        Check.notNull(builder.tickUnit, "tickUnit");
        Check.argCondition(builder.tick <= 0, "tick");
        this.scheduler = builder.scheduler;

        this.startHandler = builder.startHandler;
        this.tickHandler = builder.tickHandler;
        this.finishHandler = builder.finishHandler;
        this.cancelHandler = builder.cancelHandler;
        this.stopCount = builder.stopCount;
        this.tick = builder.tick;
        this.tickUnit = builder.tickUnit;
        this.status = CounterStatus.IDLING;
        this.step = this.stopCount > builder.startCount ? 1 : -1;
        this.startCount = builder.startCount - this.step;
    }

    @Override
    public void start() {
        if (this.task != null) {
            throw new IllegalStateException("This counter is already running");
        }

        this.runnable = CountingRunnable
            .builder()
            .step(this.step)
            .initialCount(this.startCount)
            .condition(this::condition)
            .callback(this::tick)
            .build();
        this.task = this.scheduler
            .buildTask(new CatchingRunnable(this.runnable))
            .repeat(this.tick, this.tickUnit)
            .schedule();

        this.status = CounterStatus.RUNNING;
        this.handleStart();
    }

    @Override
    public void pause() {
        if (this.status != CounterStatus.RUNNING) {
            return;
        }
        this.status = CounterStatus.PAUSED;
    }

    @Override
    public void resume() {
        if (this.status != CounterStatus.PAUSED) {
            return;
        }
        this.status = CounterStatus.RUNNING;
    }

    @Override
    public void stop() {
        if (this.status == CounterStatus.IDLING) {
            return;
        }
        this.cancel(this::handleCancel);
    }

    @Override
    public boolean isPaused() {
        return this.status == CounterStatus.PAUSED;
    }

    @Override
    public boolean isRunning() {
        return this.status == CounterStatus.RUNNING;
    }

    @Override
    public long tickedCount() {
        return (this.startCount - this.runnable.count()) * -this.step;
    }

    @Override
    public long currentCount() {
        return this.runnable.count();
    }

    @Override
    public void currentCount(final long count) {
        this.runnable.count(count);
    }

    @Override
    public @NotNull TimeUnit tickUnit() {
        return this.tickUnit;
    }

    @Override
    public @NotNull CounterStatus status() {
        return this.status;
    }

    public long startCount() {
        return this.startCount;
    }

    @Override
    public long stopCount() {
        return this.stopCount;
    }

    @Override
    public long tickValue() {
        return this.tick;
    }

    private void handleStart() {
        if (this.startHandler == null) {
            return;
        }
        this.startHandler.accept(this);
    }

    private void handleTick() {
        if (this.tickHandler == null) {
            return;
        }
        this.tickHandler.accept(this);
    }

    private void handleFinish() {
        if (this.finishHandler == null) {
            return;
        }
        this.finishHandler.accept(this);
    }

    private void handleCancel() {
        if (this.cancelHandler == null) {
            return;
        }
        this.cancelHandler.accept(this);
    }

    private void cancel(final Runnable callback) {
        this.status = CounterStatus.IDLING;
        if (this.task == null) {
            return;
        }
        this.task.cancel();
        this.task = null;
        if (callback == null) {
            return;
        }
        callback.run();
    }

    private boolean condition() {
        if (this.status != CounterStatus.RUNNING) {
            return false;
        }

        this.handleTick();
        return true;
    }

    private void tick() {
        if (this.step * (this.step - this.runnable.count() + this.stopCount) > 0) {
            return;
        }

        this.handleFinish();
        this.cancel(null);
    }

    static final class BuilderImpl implements Counter.Builder {

        private final Scheduler scheduler;
        private long startCount;
        private long stopCount;
        private long tick;
        private TimeUnit tickUnit;

        private Consumer<Counter> startHandler;
        private Consumer<Counter> tickHandler;
        private Consumer<Counter> finishHandler;
        private Consumer<Counter> cancelHandler;

        BuilderImpl(final Scheduler scheduler) {
            this.scheduler = scheduler;
        }

        @Override
        public Counter.@NotNull Builder startCount(final long startCount) {
            this.startCount = startCount;
            return this;
        }

        @Override
        public Counter.@NotNull Builder stopCount(final long stopCount) {
            this.stopCount = stopCount;
            return this;
        }

        @Override
        public Counter.@NotNull Builder tick(final long tick, @NotNull final TimeUnit tickUnit) {
            Check.notNull(tickUnit, "tickUnit");
            Check.argCondition(tick <= 0, "tick must be positive");
            this.tick = tick;
            this.tickUnit = tickUnit;
            return this;
        }

        @Override
        public Counter.@NotNull Builder startCallback(final Consumer<Counter> startCallback) {
            this.startHandler = startCallback;
            return this;
        }

        @Override
        public Counter.@NotNull Builder tickCallback(final Consumer<Counter> tickCallback) {
            this.tickHandler = tickCallback;
            return this;
        }

        @Override
        public Counter.@NotNull Builder finishCallback(final Consumer<Counter> finishCallback) {
            this.finishHandler = finishCallback;
            return this;
        }

        @Override
        public Counter.@NotNull Builder cancelCallback(final Consumer<Counter> cancelCallback) {
            this.cancelHandler = cancelCallback;
            return this;
        }

        @Override
        public @NotNull Counter build() {
            return new CounterImpl(this);
        }
    }
}
