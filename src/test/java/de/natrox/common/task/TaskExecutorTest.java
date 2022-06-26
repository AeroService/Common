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

package de.natrox.common.task;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskExecutorTest {

    private static TaskExecutor taskExecutor;

    @BeforeAll
    static void createTaskExecutor() {
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

}
