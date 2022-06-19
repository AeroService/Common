package de.natrox.common.taskchain;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

class TaskChainTest {

    @Test
    void test() {
        TaskExecutor executor = new CachedTaskExecutor();

        TaskChain taskChain = new TaskChainImpl(executor)
            .sync(() -> {

            })
            .async(() -> {

            })
            .delay(5, TimeUnit.SECONDS)
            .sync(() -> {

            });

        taskChain.run();
    }

}
