package de.natrox.common.concurrent;

public final class SimpleTaskBatchFactory implements TaskBatch.Factory {

    @Override
    public TaskBatch createTaskBatch() {
        return new SimpleTaskBatch(new SimpleTaskBatchExecutor());
    }

}
