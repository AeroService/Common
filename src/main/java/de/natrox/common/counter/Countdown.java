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

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

non-sealed class Countdown implements Counter {

    protected final long startCount;
    protected final long stopCount;
    protected final long tick;
    protected final TimeUnit tickUnit;
    private final Scheduler scheduler;
    private final Consumer<Counter> startHandler;
    private final Consumer<Counter> tickHandler;
    private final Consumer<Counter> finishHandler;
    private final Consumer<Counter> cancelHandler;

    private Task task;
    private long currentCount;
    private CounterStatus status;

    Countdown(
        @NotNull Scheduler scheduler,
        long startCount,
        long stopCount,
        long tick,
        @NotNull TimeUnit tickUnit,
        Consumer<Counter> startHandler,
        Consumer<Counter> tickHandler,
        Consumer<Counter> finishHandler,
        Consumer<Counter> cancelHandler
    ) {
        Check.notNull(scheduler, "scheduler");
        Check.notNull(tickUnit, "tickUnit");
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
    }

    @Override
    public void start() {
        if(this.task != null)
            throw new IllegalStateException("The counter is already running");

        this.handleStart();
        this.currentCount = this.startCount + 1;

        this.task = this.scheduler
            .buildTask(new CatchingRunnable(this::tick))
            .repeat(this.tick, this.tickUnit)
            .schedule();

        this.status = CounterStatus.RUNNING;
    }

    @Override
    public void pause() {
        if(status == CounterStatus.RUNNING)
            this.status = CounterStatus.PAUSED;
    }

    @Override
    public void resume() {
        if(status == CounterStatus.PAUSED)
            this.status = CounterStatus.RUNNING;
    }

    @Override
    public void stop() {
        if(status != CounterStatus.IDLING) {
            cancel();
        }
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
        return this.currentCount;
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
    public TimeUnit tickUnit() {
        return this.tickUnit;
    }

    @Override
    public CounterStatus status() {
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

    protected void handleStart() {
        if(this.startHandler == null)
            return;
        this.startHandler.accept(this);
    }

    protected void handleTick() {
        if(this.tickHandler == null)
            return;
        this.tickHandler.accept(this);
    }

    protected void handleFinish() {
        if(this.finishHandler == null)
            return;
        this.finishHandler.accept(this);
    }

    protected void handleCancel() {
        if(this.cancelHandler == null)
            return;
        this.cancelHandler.accept(this);
    }

    private void cancel() {
        this.status = CounterStatus.IDLING;

        if(this.task != null) {
            this.task.cancel();
            this.handleCancel();
        }
        this.task = null;
    }

    private void tick() {
        if(this.status == CounterStatus.RUNNING) {

            if(this.currentCount > this.stopCount) {
                this.currentCount -= 1;
                this.handleTick();
            }

            if(this.currentCount == this.stopCount) {
                this.status = CounterStatus.IDLING;
                this.handleFinish();
                this.cancel();
            }

        }
    }
}
