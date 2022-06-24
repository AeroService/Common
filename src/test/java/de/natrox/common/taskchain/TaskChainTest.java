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

package de.natrox.common.taskchain;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class TaskChainTest {

    @Test
    void factoryTest() {
        TaskChain.Factory factory = TaskChain.createFactory(CachedTaskExecutor.create());
        TaskChain taskChain = factory.create();
        assertNotNull(taskChain);
    }

    @Test
    void multipleRunTest() {
        TaskChain.Factory factory = TaskChain.createFactory(CachedTaskExecutor.create());
        TaskChain taskChain = factory.create();

        assertDoesNotThrow(() -> taskChain.run());
        assertThrows(IllegalStateException.class, taskChain::run);
    }

    @Test
    void delayTest() throws InterruptedException {
        TaskChain.Factory factory = TaskChain.createFactory(CachedTaskExecutor.create());
        CountDownLatch latch = new CountDownLatch(2);

        TaskChain taskChain = factory.create()
            .sync(latch::countDown)
            .delay(10, TimeUnit.MILLISECONDS)
            .sync(latch::countDown);

        taskChain.run();

        Thread.sleep(5);
        assertEquals(1, latch.getCount());

        latch.await();
        assertEquals(0, latch.getCount());
    }

    @Test
    void callbackTest() throws InterruptedException {
        TaskChain.Factory factory = TaskChain.createFactory(CachedTaskExecutor.create());
        TaskChain taskChain = factory.create();

        CountDownLatch latch = new CountDownLatch(1);

        taskChain.run(latch::countDown);
        latch.await();

        assertEquals(0, latch.getCount());
    }

}
