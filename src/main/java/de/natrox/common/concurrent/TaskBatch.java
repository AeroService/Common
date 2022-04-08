package de.natrox.common.concurrent;

import de.natrox.common.runnable.CatchingRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public final class TaskBatch {

    private final TaskBatchExecutor executor;
    private final List<TaskInfo> tasks;
    private final AtomicBoolean locked;
    private Runnable callback;

    protected TaskBatch(TaskBatchExecutor executor) {
        this.executor = executor;

        this.tasks = new ArrayList<>();
        this.locked = new AtomicBoolean(false);
    }

    public @NotNull TaskBatch sync(@NotNull Runnable runnable) {
        Objects.requireNonNull(runnable, "Runnable can't be null!");
        this.addTask(TaskType.SYNC, runnable, 0);
        return this;
    }

    public @NotNull TaskBatch async(@NotNull Runnable runnable) {
        Objects.requireNonNull(runnable, "Runnable can't be null!");
        this.addTask(TaskType.ASYNC, runnable, 0);
        return this;
    }

    public @NotNull TaskBatch wait(long delay, @NotNull TimeUnit timeUnit) {
        Objects.requireNonNull(timeUnit, "timeUnit can't be null!");
        this.addTask(TaskType.WAIT, null, timeUnit.toMillis(delay));
        return this;
    }

    public void execute(@Nullable Runnable callback) {
        this.callback = callback;
        this.executor.async(new CatchingRunnable(this::runBatch));
    }

    public void execute() {
        execute(null);
    }

    public @NotNull List<Runnable> interrupt() {
        return executor.interrupt();
    }

    private void runBatch() {
        for (var i = 0; i < tasks.size(); i++) {
            while (locked.get()) {
            }
            var task = tasks.get(i);
            locked.set(true);
            if (task.taskType.equals(TaskType.SYNC))
                executor.sync(task.runnable());
            else if (task.taskType.equals(TaskType.ASYNC))
                executor.async(task.runnable());
            else {
                executor.async(new CatchingRunnable(() -> {
                    try {
                        Thread.sleep(task.delay());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    locked.set(false);
                }));
            }
        }
        if (callback != null)
            callback.run();
        executor.shutdown();
    }

    private void addTask(@NotNull TaskType taskType, @Nullable Runnable runnable, long delay) {
        Objects.requireNonNull(taskType, "taskType can't be null!");
        tasks.add(new TaskInfo(delay, taskType, () -> {
            if (runnable != null)
                runnable.run();
            locked.set(false);
        }));
    }

    enum TaskType {
        SYNC,
        ASYNC,
        WAIT
    }

    record TaskInfo(long delay, TaskType taskType, Runnable runnable) {
    }
}
