package de.natrox.common.concurrent;

public final class SimpleTaskBatchFactory implements TaskBatchFactory {

    @Override
    public TaskBatch createTaskBatch() {
        return new SimpleTaskBatch(new SimpleTaskBatchExecutor());
    }
}
