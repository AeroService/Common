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

non-sealed class Countdown implements Counter {

    protected final long startTime;
    protected final long stopTime;
    protected final long tick;
    protected final TimeUnit tickUnit;
    private final Scheduler scheduler;

    private Task task;
    private long currentTime;
    private CounterStatus status;

    public Countdown(@NotNull Scheduler scheduler, long startTime, long stopTime, long tick, @NotNull TimeUnit tickUnit) {
        Check.notNull(scheduler, "scheduler");
        Check.notNull(tickUnit, "tickUnit");
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.tick = tick;
        this.tickUnit = tickUnit;
        this.scheduler = scheduler;
        this.status = CounterStatus.IDLING;
    }

    @Override
    public void start() {
        if(this.task != null) {
            throw new IllegalStateException("The counter is already running");
        }

        this.handleStart();
        this.currentTime = this.startTime + 1;

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
        if(status == CounterStatus.PAUSED)
            this.status = CounterStatus.IDLING;
    }

    @Override
    public boolean isPaused() {
        return status ==  CounterStatus.PAUSED;
    }

    @Override
    public boolean isRunning() {
        return status == CounterStatus.RUNNING;
    }

    private void cancel() {
        this.status = CounterStatus.IDLING;

        if(this.task != null)
            this.task.cancel();
        this.task = null;
    }

    public long tickedTime() {return this.currentTime;}

    @Override
    public long currentTime() {
        return this.currentTime;
    }

    @Override
    public void currentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    @Override
    public TimeUnit tickUnit() {
        return tickUnit;
    }

    @Override
    public CounterStatus status() {
        return status;
    }

    public long startTime() {
        return this.startTime;
    }

    @Override
    public long stopTime() {
        return this.stopTime;
    }

    @Override
    public long tickValue() {
        return this.tick;
    }

    protected void handleStart() {

    }

    protected void handleTick() {

    }

    protected void handleFinish() {

    }

    protected void handleCancel() {

    }

    private void tick() {
        if(this.status == CounterStatus.RUNNING) {

            if(this.currentTime > this.stopTime) {
                this.currentTime -= this.tick;
                this.handleTick();
            }

            if(this.currentTime == this.stopTime) {
                this.status = CounterStatus.IDLING;
                this.handleFinish();
                this.cancel();
            }

        }
    }
}
