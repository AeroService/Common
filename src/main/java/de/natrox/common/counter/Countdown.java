package de.natrox.common.counter;

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

    public Countdown(@NotNull Scheduler scheduler, int startTime, int stopTime, int tick, TimeUnit timeUnit) {
        this.scheduler = scheduler;
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.tick = tick;
        this.timeUnit = timeUnit;
    }

    @Override
    public void start() {
        if (task != null) {
            throw new IllegalStateException("The counter is already running");
        }

        handleStart();
        currentTime = startTime + 1;

        this.task = scheduler
            .buildTask(new CatchingRunnable(() -> {
                if (!isPaused() && isRunning()) {

                    if (currentTime > stopTime) {
                        currentTime--;
                        handleTick();
                    }

                    if (currentTime == stopTime) {
                        setRunning(false);
                        handleFinish();
                        cancel();
                    }

                }
            }))
            .repeat(tick, timeUnit)
            .schedule();

        setRunning(true);
        setPaused(false);
    }

    @Override
    public void pause() {
        setPaused(true);
        setRunning(false);
    }

    @Override
    public void resume() {
        setPaused(false);
        setRunning(true);
    }

    @Override
    public void stop() {
        if (isRunning()) {
            cancel();
            handleCancel();
        }
    }

    private void cancel() {
        setRunning(false);

        if (task != null)
            task.cancel();
        task = null;
    }

    public int tickedTime() {
        return currentTime;
    }

    @Override
    public int currentTime() {
        return currentTime;
    }

    @Override
    public boolean isPaused() {
        return paused;
    }

    @Override
    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void setRunning(boolean running) {
        this.running = running;
    }

    public int startTime() {
        return startTime;
    }

    public void currentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    protected abstract void handleStart();

    protected abstract void handleTick();

    protected abstract void handleFinish();

    protected abstract void handleCancel();
}
