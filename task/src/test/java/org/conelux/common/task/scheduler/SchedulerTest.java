/*
 * Copyright 2020-2022 Conelux
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

package org.conelux.common.task.scheduler;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Test;

class SchedulerTest {

    private static final Scheduler scheduler = Scheduler.create();

    @Test
    public void testSchedule() {
        AtomicBoolean result = new AtomicBoolean(false);
        assertFalse(result.get(), "Task should not executed yet");

        scheduler.submitTask(() -> {
            result.set(true);

            return TaskSchedule.stop();
        });

        assertTrue(result.get(), "Task didn't get executed");
    }

    @Test
    public void testDelayedTask() throws InterruptedException {
        AtomicBoolean result = new AtomicBoolean(false);
        assertFalse(result.get(), "Task should not executed yet");

        scheduler
            .buildTask(() -> result.set(true))
            .delay(TaskSchedule.seconds(1))
            .schedule();

        Thread.sleep(100);
        assertFalse(result.get(), "900ms remaining");
        Thread.sleep(1200);
        assertTrue(result.get(), "Tick task must be executed after 1 second");
    }

    @Test
    public void testRepeatedTask() {

    }

    @Test
    public void testImmediateTask() {
        AtomicBoolean result = new AtomicBoolean(false);
        assertFalse(result.get(), "Task should not executed yet");

        scheduler.submitTask(() -> {
            result.set(true);

            return TaskSchedule.immediate();
        });

        assertTrue(result.get());
        result.set(false);
        assertFalse(result.get());
    }

    @Test
    public void testCancelTask() throws InterruptedException {
        AtomicBoolean result = new AtomicBoolean(false);
        Task task = scheduler
            .buildTask(() -> result.set(true))
            .delay(Duration.ofMillis(1))
            .schedule();

        assertTrue(task.isAlive(), "Task should still be alive");
        task.cancel();
        assertFalse(task.isAlive(), "Task should not be alive anymore");
        Thread.sleep(10L);
        assertFalse(result.get(), "Task should be cancelled");
    }

    @Test
    public void testFutureTask() throws InterruptedException {
        CompletableFuture<Void> future = new CompletableFuture<>();
        AtomicBoolean result = new AtomicBoolean(false);
        scheduler
            .buildTask(() -> result.set(true))
            .delay(TaskSchedule.future(future))
            .schedule();

        assertFalse(result.get(), "Future is not completed yet");
        future.complete(null);
        Thread.sleep(10L);
        assertTrue(result.get(), "Future should be completed");
    }
}
