/*
 * Copyright 2020-2022 NatroxMC team
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

import de.natrox.common.base.Check;
import de.natrox.common.runnable.CatchingRunnable;
import de.natrox.common.scheduler.Scheduler;
import de.natrox.common.scheduler.Task;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public abstract non-sealed class Countdown implements Counter {

    protected final int startTime;
    protected final int stopTime;
    protected final int tick;
    protected final TimeUnit timeUnit;
    private final Scheduler scheduler;

    private Task task;
    private int currentTime;
    private boolean paused;
    private boolean running;

    public Countdown(@NotNull Scheduler scheduler, int startTime, int stopTime, int tick, @NotNull TimeUnit timeUnit) {
        Check.notNull(scheduler, "scheduler");
        Check.notNull(timeUnit, "timeUnit");
        this.scheduler = scheduler;
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.tick = tick;
        this.timeUnit = timeUnit;
    }

    @Override
    public void start() {
        if (this.task != null) {
            throw new IllegalStateException("The counter is already running");
        }

        this.handleStart();
        this.currentTime = this.startTime + 1;

        this.task = this.scheduler
            .buildTask(new CatchingRunnable(() -> {
                if (!this.isPaused() && this.isRunning()) {

                    if (this.currentTime > this.stopTime) {
                        this.currentTime -= this.tick;
                        this.handleTick();
                    }

                    if (this.currentTime == this.stopTime) {
                        this.running = false;
                        this.handleFinish();
                        this.cancel();
                    }

                }
            }))
            .repeat(this.tick, this.timeUnit)
            .schedule();

        this.running = true;
        this.paused = false;
    }

    @Override
    public void pause() {
        this.paused = true;
        this.running = false;
    }

    @Override
    public void resume() {
        this.paused = false;
        this.running = true;
    }

    @Override
    public void stop() {
        if (this.isRunning()) {
            this.cancel();
            this.handleCancel();
        }
    }

    private void cancel() {
        this.running = false;

        if (this.task != null)
            this.task.cancel();
        this.task = null;
    }

    public int tickedTime() {
        return this.currentTime;
    }

    @Override
    public int currentTime() {
        return this.currentTime;
    }

    @Override
    public boolean isPaused() {
        return this.paused;
    }

    @Override
    public boolean isRunning() {
        return this.running;
    }

    public int startTime() {
        return this.startTime;
    }

    public void currentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    protected abstract void handleStart();

    protected abstract void handleTick();

    protected abstract void handleFinish();

    protected abstract void handleCancel();
}
