package de.natrox.common.scheduler;

import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

final class TaskImpl implements Task {

    private final int id;
    private final @NotNull Supplier<TaskSchedule> task;
    private final @NotNull SchedulerImpl owner;

    volatile boolean alive;
    volatile boolean parked;

    TaskImpl(int id,
             @NotNull Supplier<TaskSchedule> task,
             @NotNull SchedulerImpl owner
    ) {
        this.id = id;
        this.task = task;
        this.owner = owner;
        this.alive = true;
    }

    @Override
    public void cancel() {
        this.alive = false;
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    public int id() {
        return id;
    }

    public @NotNull Supplier<TaskSchedule> task() {
        return task;
    }

    public @NotNull SchedulerImpl owner() {
        return owner;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (TaskImpl) obj;
        return this.id == that.id;
    }


    @Override
    public String toString() {
        return "TaskImpl[" +
            "id=" + id + ", " +
            "task=" + task + ", " +
            "owner=" + owner + ']';
    }

}
