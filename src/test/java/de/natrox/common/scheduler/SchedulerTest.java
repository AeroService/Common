/*
 * Copyright 2020-2022 NatroxMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.natrox.common.scheduler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.natrox.common.task.CachedTaskExecutor;
import de.natrox.common.task.TaskExecutor;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class SchedulerTest {

    private static Scheduler scheduler;

    @BeforeAll
    private static void init() {
        TaskExecutor taskExecutor = CachedTaskExecutor.create();
        scheduler = Scheduler.create(taskExecutor);
    }

    @Test
    void testBuildTask() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Task task = scheduler
            .buildTask(latch::countDown)
            .schedule();
        latch.await();
        assertEquals(TaskStatus.FINISHED, task.status(), "The task should be done executing");
    }

    @Test
    void testCancel() throws InterruptedException {
        AtomicInteger indicator = new AtomicInteger(0);
        Task task = scheduler
            .buildTask(indicator::incrementAndGet)
            .delay(100, TimeUnit.SECONDS)
            .schedule();
        task.cancel();
        Thread.sleep(200);
        assertEquals(0, indicator.get(), "The indicator value should not have changed");
        assertEquals(TaskStatus.CANCELLED, task.status(), "The task should have been cancelled");
    }

    @Test
    void testRepeatTask() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);
        Task task = scheduler
            .buildTask(latch::countDown)
            .delay(100, TimeUnit.MILLISECONDS)
            .repeat(100, TimeUnit.MILLISECONDS)
            .schedule();
        latch.await();
        task.cancel();
    }

    @Test
    void testCallback() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        scheduler
            .buildTask(() -> {

            })
            .schedule(latch::countDown);
        latch.await();

        assertEquals(0, latch.getCount());
    }

    @Test
    void testCallbackCancel() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        scheduler
            .buildTask(Assertions::fail)
            .schedule(latch::countDown)
            .cancel();
        latch.await();

        assertEquals(0, latch.getCount());
    }
}
