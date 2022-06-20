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
