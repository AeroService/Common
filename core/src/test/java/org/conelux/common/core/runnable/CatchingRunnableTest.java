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

package org.conelux.common.core.runnable;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class CatchingRunnableTest {

    private static int a;

    @Test
    void testRun() {
        CatchingRunnable runnable = new CatchingRunnable(this::check);
        a = 1;
        assertDoesNotThrow(runnable::run);
        a = 2;
        assertDoesNotThrow(runnable::run);
    }

    @Test
    void testThrowingRun() {
        CatchingRunnable runnable = new CatchingRunnable(this::check);
        a = -1;
        assertThrows(IllegalArgumentException.class,
            runnable::run, "Function should throw an exception if the arguments don't meet the conditions");
    }

    private void check() {
        if (a <= 0) {
            throw new IllegalArgumentException();
        }
    }
}
