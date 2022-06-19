package de.natrox.common.taskchain;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

class TaskChainTest {

    @Test
    void test() {
        new TaskChainImpl()
            .sync(() -> {

            })
            .async(() -> {

            })
            .delay(5, TimeUnit.SECONDS)
            .sync(() -> {

            })
            .run();
    }

}
