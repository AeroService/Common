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

package org.aero.common.core.function;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ThrowableFunctionTest {

    @Test
    void testApply() {
        final ThrowableFunction<Integer, Integer, IllegalArgumentException> function = i -> i;
        assertEquals(1, function.apply(1), "Function should return the input of 1");
        assertEquals(2, function.apply(2), "Function should return the input of 2");
    }

    @Test
    void testThrowingApply() {
        final ThrowableFunction<Integer, Integer, IllegalArgumentException> function = i -> {
            this.throwException();
            return i;
        };
        assertThrows(IllegalArgumentException.class, () ->
            function.apply(-1), "Function should throw an exception if the arguments don't meet the conditions");
    }

    @Test
    void testThrowingApply2() {
        final ThrowableFunction<Integer, Integer, Exception> function = i -> {
            throwException();
            return i;
        };
        assertThrows(Exception.class, () ->
            function.apply(-1), "Function should throw an exception if the arguments don't meet the conditions");
    }

    @Test
    void testAndThenApply() {
        final Function<Integer, Integer> andThenFunction = (a) -> (0);
        final ThrowableFunction<Integer, Integer, IllegalArgumentException> operation = i -> i;
        final ThrowableFunction<Integer, Integer, IllegalArgumentException> function = operation.andThen(andThenFunction);
        assertEquals(0, function.apply(1), "Function should return zero");
        assertEquals(0, function.apply(2), "Function should return zero");
    }

    @Test
    void testAndThenNull() {
        final ThrowableFunction<Integer, Integer, IllegalArgumentException> function = i -> {
            this.throwException();
            return i;
        };
        assertThrows(NullPointerException.class, () ->
            function.andThen(null), "Function should throw a NullPointerException if the andThen function is invalid");
    }

    @Test
    void testAndThenExecution() {
        final AtomicInteger indicator = new AtomicInteger();
        final Function<Integer, Integer> andThenFunction = (a) -> indicator.incrementAndGet();
        final ThrowableFunction<Integer, Integer, IllegalArgumentException> operation = (a) -> {
            throw new IllegalArgumentException();
        };
        final ThrowableFunction<Integer, Integer, IllegalArgumentException> function = operation.andThen(andThenFunction);
        assertThrows(IllegalArgumentException.class, () -> function.apply(1), "The operation should fail");
        assertEquals(0, indicator.get(), "AndThenFunction should not have executed since the operation failed");
    }

    private void throwException() {
        throw new IllegalArgumentException();
    }

    private int throwException2() throws Exception {
        throw new Exception();
    }
}
