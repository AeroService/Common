/*
 * Copyright 2020-2022 NatroxMC
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

package de.natrox.common.counter;

import de.natrox.common.runnable.CatchingRunnable;
import de.natrox.common.scheduler.Scheduler;
import de.natrox.common.scheduler.Task;
import de.natrox.common.validate.Check;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

final class CounterImpl implements Counter {

    private final Scheduler scheduler;

    private final long startCount;
    private final long stopCount;
    private final long tick;
    private final TimeUnit tickUnit;

    private final Consumer<Counter> startHandler;
    private final Consumer<Counter> tickHandler;
    private final Consumer<Counter> finishHandler;
    private final Consumer<Counter> cancelHandler;

    private final int step;
    private long currentCount;
    private Task task;
    private CounterStatus status;

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
        Check.notNull(scheduler, "scheduler");
        Check.notNull(tickUnit, "tickUnit");
        Check.argCondition(tick <= 0, "tick must be positive");

        this.startHandler = startHandler;
        this.tickHandler = tickHandler;
        this.finishHandler = finishHandler;
        this.cancelHandler = cancelHandler;
        this.startCount = startCount;
        this.stopCount = stopCount;
        this.tick = tick;
        this.tickUnit = tickUnit;
        this.scheduler = scheduler;
        this.status = CounterStatus.IDLING;
        this.step = stopCount > startCount ? 1 : -1;
    }

    @Override
    public void start() {
        if (this.task != null)
            throw new IllegalStateException("The counter is already running");

        this.currentCount = this.startCount - step;

        this.task = this.scheduler
            .buildTask(new CatchingRunnable(this::tick))
            .repeat(this.tick, this.tickUnit)
            .schedule();

        this.status = CounterStatus.RUNNING;

        this.handleStart();
    }

    @Override
    public void pause() {
        if (this.status != CounterStatus.RUNNING)
            return;
        this.status = CounterStatus.PAUSED;
    }

    @Override
    public void resume() {
        if (this.status != CounterStatus.PAUSED)
            return;
        this.status = CounterStatus.RUNNING;
    }

    @Override
    public void stop() {
        if (this.status == CounterStatus.IDLING)
            return;
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
    public long tickedTime() {
        return (this.startCount - this.currentCount) * -this.step;
    }

    @Override
    public long currentCount() {
        return this.currentCount;
    }

    @Override
    public void currentCount(long currentCount) {
        this.currentCount = currentCount;
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
        if (this.startHandler == null)
            return;
        this.startHandler.accept(this);
    }

    private void handleTick() {
        if (this.tickHandler == null)
            return;
        this.tickHandler.accept(this);
    }

    private void handleFinish() {
        if (this.finishHandler == null)
            return;
        this.finishHandler.accept(this);
    }

    private void handleCancel() {
        if (this.cancelHandler == null)
            return;
        this.cancelHandler.accept(this);
    }

    private void cancel(Runnable callback) {
        this.status = CounterStatus.IDLING;
        if (this.task == null)
            return;
        this.task.cancel();
        this.task = null;
        if (callback == null)
            return;
        callback.run();
    }

    private void tick() {
        if (this.status != CounterStatus.RUNNING)
            return;

        if (this.currentCount * this.step <= this.stopCount * this.step) {
            this.currentCount += this.step;
            this.handleTick();
        }

        if ((this.currentCount - this.step) * this.step >= this.stopCount * this.step) {
            this.handleFinish();
            this.cancel(null);
        }
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
