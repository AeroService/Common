package de.notion.common.concurrent;

import de.notion.common.runnable.CatchingRunnable;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public final class TaskBatch {

    private final List<TaskInfo> tasks = new ArrayList<>();
    private final ExecutorService executor = Executors.newScheduledThreadPool(4, new DefaultThreadFactory("TaskBatch"));
    private final AtomicBoolean locked = new AtomicBoolean(false);
    private final Executor batchExecutor;
    private Runnable callback;

    public TaskBatch() {
        this.batchExecutor = new Executor() {
            @Override
            public void runSync(@NotNull Runnable runnable) {
                runnable.run();
            }

            @Override
            public void runAsync(@NotNull Runnable runnable) {
                executor.execute(runnable);
            }

            @Override
            public void onFinish() {

            }
        };
    }

    public TaskBatch(Executor batchExecutor) {
        this.batchExecutor = batchExecutor;
    }

    public TaskBatch doSync(@NotNull Runnable runnable) {
        Objects.requireNonNull(runnable, "Runnable can't be null!");
        addTask(TaskType.SYNC, runnable, 0);
        return this;
    }

    public TaskBatch doAsync(@NotNull Runnable runnable) {
        Objects.requireNonNull(runnable, "Runnable can't be null!");
        addTask(TaskType.ASYNC, runnable, 0);
        return this;
    }

    public TaskBatch wait(long delay, @NotNull TimeUnit timeUnit) {
        Objects.requireNonNull(timeUnit, "timeUnit can't be null!");
        addTask(TaskType.WAIT, null, timeUnit.toMillis(delay));
        return this;
    }

    public void executeBatch(@Nullable Runnable callback) {
        this.callback = callback;
        executor.submit(new CatchingRunnable(this::runBatch));
    }

    public void executeBatch() {
        executeBatch(null);
    }

    public List<Runnable> interrupt() {
        return executor.shutdownNow();
    }

    private void runBatch() {
        for (int i = 0; i < tasks.size(); i++) {
            while (locked.get()) {
            }
            var task = tasks.get(i);
            locked.set(true);
            if (task.taskType.equals(TaskType.SYNC))
                batchExecutor.runSync(task.runnable);
            else if (task.taskType.equals(TaskType.ASYNC))
                batchExecutor.runAsync(task.runnable);
            else {
                new Thread(() -> {
                    try {
                        Thread.sleep(task.delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    locked.set(false);
                }).start();
            }
        }
        if (callback != null)
            callback.run();
        batchExecutor.onFinish();
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

    public interface Executor {

        void runSync(@NotNull Runnable runnable);

        void runAsync(@NotNull Runnable runnable);

        void onFinish();
    }

    record TaskInfo(long delay, TaskType taskType, Runnable runnable) {
    }
}