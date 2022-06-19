package de.natrox.common.taskchain;

import de.natrox.common.validate.Check;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Represents a chain of tasks
 */
public sealed interface TaskChain permits TaskChainImpl {

    static TaskChain.@NotNull Factory createFactory(@NotNull TaskExecutor taskExecutor) {
        Check.notNull(taskExecutor, "taskExecutor");
        return new TaskChainImpl.FactoryImpl(taskExecutor);
    }

    /**
     * Execute the task on the main thread
     *
     * @param task the task to execute
     * @return this TaskChain, for chaining
     */
    @NotNull TaskChain sync(@NotNull Task task);

    /**
     * Execute the task on an extra thread
     *
     * @param task the task to execute
     * @return this TaskChain, for chaining
     */
    @NotNull TaskChain async(@NotNull Task task);

    /**
     * Execute the task on the current thread
     *
     * @param task the task to execute
     * @return this TaskChain, for chaining
     */
    @NotNull TaskChain current(@NotNull Task task);

    /**
     * Execute the task on the main thread
     *
     * @param futureTask the task to execute
     * @return this TaskChain, for chaining
     */
    @NotNull TaskChain syncFuture(Task.@NotNull FutureTask futureTask);

    /**
     * Execute the task on an extra thread
     *
     * @param futureTask the task to execute
     * @return this TaskChain, for chaining
     */
    @NotNull TaskChain asyncFuture(Task.@NotNull FutureTask futureTask);

    /**
     * Execute the task on the current thread
     *
     * @param futureTask the task to execute
     * @return this TaskChain, for chaining
     */
    @NotNull TaskChain currentFuture(Task.@NotNull FutureTask futureTask);

    /**
     * Execute the task on the main thread
     *
     * @param callbackTask the task to execute
     * @return this TaskChain, for chaining
     */
    @NotNull TaskChain syncCallback(Task.@NotNull CallbackTask callbackTask);

    /**
     * Execute the task on an extra thread
     *
     * @param callbackTask the task to execute
     * @return this TaskChain, for chaining
     */
    @NotNull TaskChain asyncCallback(Task.@NotNull CallbackTask callbackTask);

    /**
     * Execute the task on the current thread
     *
     * @param callbackTask the task to execute
     * @return this TaskChain, for chaining
     */
    @NotNull TaskChain currentCallback(Task.@NotNull CallbackTask callbackTask);

    /**
     * Adds a delay to the batch execution.
     *
     * @param duration the duration of the delay before next task
     * @param timeUnit the {@link TimeUnit} in which the duration is specified
     * @return this TaskChain, for chaining
     */
    @NotNull TaskChain delay(@Range(from = 0, to = Long.MAX_VALUE) long duration, @NotNull TimeUnit timeUnit);

    /**
     * Adds a delay to the batch execution.
     *
     * @param duration the {@link Duration} of the delay before next task
     * @return this TaskChain, for chaining
     */
    default @NotNull TaskChain delay(@NotNull Duration duration) {
        Check.notNull(duration, "duration");
        return this.delay(duration.toMillis(), TimeUnit.MILLISECONDS);
    }

    /**
     * Adds a delay to the batch execution.
     *
     * @param duration     the duration of the delay before next task
     * @param temporalUnit the {@link TemporalUnit} in which the duration is specified
     * @return this TaskChain, for chaining
     */
    default @NotNull TaskChain delay(@Range(from = 0, to = Long.MAX_VALUE) long duration, @NotNull TemporalUnit temporalUnit) {
        Check.notNull(temporalUnit, "temporalUnit");
        return this.delay(Duration.of(duration, temporalUnit));
    }

    /**
     * Finished adding tasks, begins executing them with a done notifier
     *
     * @param callback the {@link Consumer} to handle when the batch has finished completion
     */
    void run(@Nullable Consumer<Boolean> callback);

    /**
     * Finished adding tasks, begins executing them with a done notifier
     *
     * @param callback the {@link Runnable} to handle when the batch has finished completion
     */
    default void run(@Nullable Runnable callback) {
        this.run(callback != null ? result -> callback.run() : null);
    }

    /**
     * Finished adding tasks, begins executing them.
     */
    default void run() {
        this.run((Consumer<Boolean>) null);
    }

    interface Factory {

        @NotNull TaskChain create();

        void shutdown();

    }

}
