/*
 * Copyright 2020-2022 NatroxMC
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.natrox.common.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaskExecutorTest {

    private static TaskExecutor taskExecutor;

    @BeforeAll
    private static void init() {
        taskExecutor = CachedTaskExecutor.create();
    }

    @BeforeEach
    private void reinit() {
        taskExecutor.shutdown();
        taskExecutor = CachedTaskExecutor.create();
    }

    @Test
    void testIsMainThread() throws InterruptedException {
        assertTrue(taskExecutor.isMainThread());

        AtomicBoolean result = new AtomicBoolean(true);
        Thread thread = new Thread(() -> result.set(taskExecutor.isMainThread()));

        thread.start();
        Thread.sleep(50);

        assertFalse(result.get());
    }

    @Test
    void testShutdown() {
        assertFalse(taskExecutor.isShutdown());
        taskExecutor.shutdown();

        assertTrue(taskExecutor.isShutdown());
    }

    @Test
    void executeMainTest() throws InterruptedException {
        AtomicInteger result = new AtomicInteger(-1);
        CountDownLatch latch = new CountDownLatch(1);
        Runnable runnable = () -> {
            result.set(taskExecutor.isMainThread() ? 1 : 0);
            latch.countDown();
        };
        taskExecutor.executeInMain(runnable);
        latch.await();
        assertEquals(1, result.get(), "The code above should have been executed in main thread");
    }

    @Test
    void executeAsyncTest() throws InterruptedException {
        AtomicInteger result = new AtomicInteger(-1);
        CountDownLatch latch = new CountDownLatch(1);
        Runnable runnable = () -> {
            result.set(taskExecutor.isMainThread() ? 1 : 0);
            latch.countDown();
        };
        taskExecutor.executeAsync(runnable);
        latch.await();
        assertEquals(0, result.get(), "The code above should have not been executed in main thread");
    }

    @Test
    void executeDelayedTest() throws InterruptedException {
        AtomicInteger result = new AtomicInteger(-1);
        CountDownLatch latch = new CountDownLatch(1);
        Runnable runnable = () -> {
            result.set(taskExecutor.isMainThread() ? 1 : 0);
            latch.countDown();
        };
        taskExecutor.executeWithDelay(runnable, 1, TimeUnit.SECONDS);
        assertEquals(-1, result.get(), "The code above should not have been executed yet");
        latch.await();
        assertEquals(0, result.get(), "The code above should have not been executed in main thread");
    }

    @Test
    void executeRepeatedTest() throws InterruptedException {
        AtomicBoolean executed = new AtomicBoolean();
        CountDownLatch latch = new CountDownLatch(3);
        Runnable runnable = () -> {
            executed.set(true);
            latch.countDown();
        };
        taskExecutor.executeInRepeat(runnable, 500, 500, TimeUnit.MILLISECONDS);
        assertFalse(executed.get(), "The code above should not have been executed yet");
        long timestamp = System.currentTimeMillis();
        latch.await();
        assertTrue(System.currentTimeMillis() - timestamp >= 500 * 3,
            "The execution of the three tasks should have taken longer than 1.5 seconds");
        assertTrue(executed.get(), "The code above should have been executed yet");
    }
}
