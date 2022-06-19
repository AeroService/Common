package de.natrox.common.taskchain;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

class TaskChainTest {

    @Test
    void test() {
        TaskChain.Factory factory = TaskChain.createFactory(CachedTaskExecutor.create());

        TaskChain taskChain = factory
            .create()
            .sync(() -> {
                System.out.println("1");
            })
            .async(() -> {
                System.out.println("2");
            })
            .syncCallback(callback -> {
                System.out.println("Callback");
                callback.run();
            })
            .syncFuture(() -> CompletableFuture.runAsync(() -> System.out.println("Future")))
            .delay(5, TimeUnit.SECONDS)
            .sync(() -> {
                System.out.println("3");
            });

        taskChain.run(() -> System.out.println("Done"));

        while (true) {

        }
    }

}
