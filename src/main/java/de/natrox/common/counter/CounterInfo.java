package de.natrox.common.counter;

import java.util.Objects;

@SuppressWarnings({"ClassCanBeRecord"})
public final class CounterInfo {

    private final CounterStatus status;
    private final long tickedTime;
    private final long currentTime;

    CounterInfo(CounterStatus status, long tickedTime, long currentTime) {
        this.status = status;
        this.tickedTime = tickedTime;
        this.currentTime = currentTime;
    }

    public CounterStatus status() {
        return this.status;
    }

    public long tickedTime() {
        return this.tickedTime;
    }

    public long currentTime() {
        return this.currentTime;
    }

    public boolean isRunning() {
        return this.status == CounterStatus.RUNNING;
    }

    public boolean isPaused() {
        return this.status == CounterStatus.PAUSED;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(obj == null || obj.getClass() != this.getClass()) return false;
        var that = (CounterInfo) obj;
        return Objects.equals(this.status, that.status) &&
            this.tickedTime == that.tickedTime &&
            this.currentTime == that.currentTime;
    }
}
