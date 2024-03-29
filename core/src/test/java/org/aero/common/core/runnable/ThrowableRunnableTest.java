/*
 * Copyright 2020-2023 AeroService
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

package org.aero.common.core.runnable;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ThrowableRunnableTest {

    @Test
    void testRun() {
        final AtomicBoolean result = new AtomicBoolean();
        final ThrowableRunnable<IllegalArgumentException> runnable = () -> result.set(true);

        assertFalse(result.get());
        assertDoesNotThrow(runnable::run);
        assertTrue(result.get());
    }

    @Test
    void testThrowingRun() {
        final ThrowableRunnable<Exception> runnable = this::throwException;
        assertThrows(IllegalArgumentException.class,
            runnable::run, "Runnable should throw an exception if the arguments don't meet the conditions");
    }

    @Test
    void testThrowingRun2() {
        final ThrowableRunnable<Exception> runnable = this::throwException2;
        assertThrows(Exception.class,
            runnable::run, "Runnable should throw an exception if the arguments don't meet the conditions");
    }

    private void throwException() {
        throw new IllegalArgumentException();
    }

    private void throwException2() throws Exception {
        throw new Exception();
    }
}
