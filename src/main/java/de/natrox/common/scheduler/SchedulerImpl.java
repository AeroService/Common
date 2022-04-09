package de.natrox.common.scheduler;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

final class SchedulerImpl implements Scheduler {

    private final ExecutorService taskService;
    private final ScheduledExecutorService timerExecutionService;
    private final Set<Task> tasks = new LinkedHashSet<>();

    public SchedulerImpl() {
        this.taskService = Executors.newCachedThreadPool(runnable -> {
            var thread = new Thread(runnable);
            thread.setName("Task Scheduler - #" + thread.getId());
            return thread;
        });
        this.timerExecutionService = Executors.newSingleThreadScheduledExecutor(runnable -> {
            var thread = new Thread(runnable);
            thread.setName("Task Scheduler Timer");
            return thread;
        });
    }

    @Override
    public @NotNull Task.Builder buildTask(@NotNull Runnable runnable) {
        Preconditions.checkNotNull(runnable, "runnable");
        return new TaskBuilderImpl(this, runnable);
    }

    public boolean shutdown() throws InterruptedException {
        Collection<Task> terminating;
        synchronized (tasks) {
            terminating = Collections.unmodifiableSet(tasks);
        }
        for (var task : terminating) {
            task.cancel();
        }
        timerExecutionService.shutdown();
        taskService.shutdown();
        return taskService.awaitTermination(10, TimeUnit.SECONDS);
    }

    public ExecutorService taskService() {
        return this.taskService;
    }

    public ScheduledExecutorService timerExecutionService() {
        return this.timerExecutionService;
    }

    public Set<Task> tasks() {
        return this.tasks;
    }
}
