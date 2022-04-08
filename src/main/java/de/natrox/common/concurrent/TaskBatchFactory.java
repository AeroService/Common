package de.natrox.common.concurrent;

/**
 * Represents a factory for {@link TaskBatch}
 */
public interface TaskBatchFactory {

    /**
     * Creates a new {@link TaskBatch}.
     *
     * @return the created {@link TaskBatch}
     */
    TaskBatch createTaskBatch();

}
