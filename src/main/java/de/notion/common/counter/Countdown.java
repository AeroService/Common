package de.notion.common.counter;

import de.notion.common.counter.event.CountEventProvider;

import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public abstract class Countdown implements Counter, CountEventProvider {

    protected final int startTime;
    protected final int stopTime;
    protected final int tick;
    protected final TimeUnit timeUnit;
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    protected Optional<ScheduledFuture<?>> optionalTask;

    private int currentTime;
    private boolean paused;
    private boolean running;

    public Countdown(int startTime, int stopTime, int tick, TimeUnit timeUnit) {
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.tick = tick;
        this.timeUnit = timeUnit;
        this.optionalTask = Optional.empty();
    }

    @Override
    public void start() {
        if (optionalTask.isPresent()) {
            throw new IllegalStateException("The counter is already running");
        }

        onStart(this);
        currentTime = startTime + 1;

        this.optionalTask = Optional.of(executorService.scheduleAtFixedRate(() -> {
            if (!isPaused() && isRunning()) {

                if (currentTime > stopTime) {
                    currentTime--;
                    onTick(this);
                }

                if (currentTime == stopTime) {
                    setRunning(false);
                    onFinish(this);
                    cancel();
                }

            }
        }, 0, tick, timeUnit));

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
            onCancel(this);
        }
    }

    private void cancel() {
        setRunning(false);

        optionalTask.ifPresent(scheduledFuture -> scheduledFuture.cancel(true));
        optionalTask = Optional.empty();
    }

    public int getTickedTime() {
        return currentTime;
    }

    @Override
    public int getCurrentTime() {
        return currentTime;
    }

    @Override
    public boolean isPaused() {
        return paused;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    @Override
    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    @Override
    public void setRunning(boolean running) {
        this.running = running;
    }
}
