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

package de.natrox.common.runnable;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class RunnableTest {

    @Test
    void catchingTest() {
        assertThrows(IllegalArgumentException.class, () -> new CatchingRunnable(null));
        assertDoesNotThrow(() -> new CatchingRunnable(() -> {
        }));

        {
            AtomicInteger number = new AtomicInteger();
            CatchingRunnable catchingRunnable = new CatchingRunnable(number::incrementAndGet);
            assertDoesNotThrow(catchingRunnable::run);
            assertEquals(1, number.get());
        }
        {
            CatchingRunnable catchingRunnable = new CatchingRunnable(() -> {
                throw new RuntimeException();
            });
            assertThrows(RuntimeException.class, catchingRunnable::run);
        }
    }
}
