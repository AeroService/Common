/*
 * Copyright 2020-2022 Conelux
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

package org.conelux.common.core.counter;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import org.conelux.common.core.runnable.CatchingRunnable;
import org.conelux.common.core.scheduler.Scheduler;
import org.conelux.common.core.scheduler.Task;
import org.conelux.common.core.validate.Check;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    private final AtomicLong currentCount;

    private volatile Task task;
    private volatile CounterStatus status;

    CounterImpl(
        Scheduler scheduler,
        long startCount,
        long stopCount,
        long tick,
        TimeUnit tickUnit,
        Consumer<Counter> startHandler,
        Consumer<Counter> tickHandler,
        Consumer<Counter> finishHandler,
        Consumer<Counter> cancelHandler
    ) {
        Check.notNull(tickUnit, "tickUnit");
        Check.argCondition(tick <= 0, "tick");
        this.scheduler = scheduler;

        this.startHandler = startHandler;
        this.tickHandler = tickHandler;
        this.finishHandler = finishHandler;
        this.cancelHandler = cancelHandler;
        this.startCount = startCount;
        this.stopCount = stopCount;
        this.tick = tick;
        this.tickUnit = tickUnit;
        this.status = CounterStatus.IDLING;
        this.step = stopCount > startCount ? 1 : -1;
        this.currentCount = new AtomicLong();
    }

    @Override
    public void start() {
        if (this.task != null) {
            throw new IllegalStateException("This counter is already running");
        }

        this.currentCount.set(this.startCount - step);
        this.task = this.scheduler
            .buildTask(new CatchingRunnable(this::tick))
            .repeat(this.tick, this.tickUnit)
            .schedule();

        this.status = CounterStatus.RUNNING;
        this.handleStart();
        this.tick();
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
        return (this.startCount - this.currentCount.get()) * -this.step;
    }

    @Override
    public long currentCount() {
        return this.currentCount.get();
    }

    @Override
    public void setCurrentCount(long count) {
        this.currentCount.set(count);
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

    private void cancel(Runnable callback) {
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

    private void tick() {
        if (this.status != CounterStatus.RUNNING) {
            return;
        }

        if (this.currentCount.get() * this.step < this.stopCount * this.step) {
            this.currentCount.addAndGet(this.step);
            this.handleTick();
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

        BuilderImpl(Scheduler scheduler) {
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
        public Counter.@NotNull Builder startCallback(@Nullable Consumer<Counter> startCallback) {
            this.startHandler = startCallback;
            return this;
        }

        @Override
        public Counter.@NotNull Builder tickCallback(@Nullable Consumer<Counter> tickCallback) {
            this.tickHandler = tickCallback;
            return this;
        }

        @Override
        public Counter.@NotNull Builder finishCallback(@Nullable Consumer<Counter> finishCallback) {
            this.finishHandler = finishCallback;
            return this;
        }

        @Override
        public Counter.@NotNull Builder cancelCallback(@Nullable Consumer<Counter> cancelCallback) {
            this.cancelHandler = cancelCallback;
            return this;
        }

        @Override
        public @NotNull Counter build() {
            return new CounterImpl(
                this.scheduler,
                this.startCount,
                this.stopCount,
                this.tick,
                this.tickUnit,
                this.startHandler,
                this.tickHandler,
                this.finishHandler,
                this.cancelHandler
            );
        }
    }
}
