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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ThrowableRunnableTest {

    private static int a;

    @Test
    void defaultRunTest1() {
        ThrowableRunnable<IllegalArgumentException> runnable = this::check;
        a = 1;
        assertDoesNotThrow(runnable::run);
        a = 2;
        assertDoesNotThrow(runnable::run);
    }

    @Test
    void defaultRunTest2() {
        ThrowableRunnable<Exception> runnable = this::exceptionCheck;
        a = 1;
        assertDoesNotThrow(runnable::run, "Runnable should not throw exception as the arguments are valid");
        a = 2;
        assertDoesNotThrow(runnable::run, "Runnable should not throw exception as the arguments are valid");
    }

    @Test
    void exceptionRunTest1() {
        ThrowableRunnable<Exception> runnable = this::check;
        a = -1;
        assertThrows(IllegalArgumentException.class,
            runnable::run, "Runnable should throw an exception if the arguments don't meet the conditions");
    }

    @Test
    void exceptionRunTest2() {
        ThrowableRunnable<Exception> runnable = this::check;
        a = -1;
        assertThrows(IllegalArgumentException.class,
            runnable::run, "Runnable should throw an exception if the arguments don't meet the conditions");
    }

    private void check() {
        if (a <= 0)
            throw new IllegalArgumentException();
    }

    private void exceptionCheck() throws Exception {
        if (a <= 0)
            throw new Exception();
    }
}
