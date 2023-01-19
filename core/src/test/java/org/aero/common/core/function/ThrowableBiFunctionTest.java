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

class ThrowableBiFunctionTest {

    @Test
    void testApply() {
        final ThrowableBiFunction<Integer, Integer, Integer, IllegalArgumentException> function = Integer::sum;
        assertEquals(3, function.apply(1, 2), "Function should return the input sum of 3");
        assertEquals(5, function.apply(2, 3), "Function should return the input sum of 5");
    }

    @Test
    void testThrowingApply() {
        final ThrowableBiFunction<Integer, Integer, Integer, IllegalArgumentException> function = (i, j) -> {
            this.throwException();
            return i + j;
        };
        assertThrows(IllegalArgumentException.class, () ->
            function.apply(-5, 3), "Function should throw an exception if the arguments don't meet the conditions");
    }

    @Test
    void testThrowingApply2() {
        final ThrowableBiFunction<Integer, Integer, Integer, Exception> function = (i, j) -> {
            this.throwException2();
            return i + j;
        };
        assertThrows(Exception.class, () ->
            function.apply(-5, 3), "Function should throw an exception if the arguments don't meet the conditions");
    }

    @Test
    void testAndThenApply() {
        final Function<Integer, Integer> andThenFunction = (a) -> (0);
        final ThrowableBiFunction<Integer, Integer, Integer, IllegalArgumentException> operation = Integer::sum;
        final ThrowableBiFunction<Integer, Integer, Integer, IllegalArgumentException> function = operation.andThen(
            andThenFunction);
        assertEquals(0, function.apply(1, 2), "Function should return zero");
        assertEquals(0, function.apply(2, 3), "Function should return zero");
    }

    @Test
    void testAndThenNull() {
        final ThrowableBiFunction<Integer, Integer, Integer, IllegalArgumentException> function = (i, j) -> {
            this.throwException();
            return i + j;
        };
        assertThrows(NullPointerException.class, () ->
            function.andThen(null), "Function should throw a NullPointerException if the andThen function is invalid");
    }

    @Test
    void testAndThenExecution() {
        final AtomicInteger indicator = new AtomicInteger();
        final Function<Integer, Integer> andThenFunction = (a) -> indicator.incrementAndGet();
        final ThrowableBiFunction<Integer, Integer, Integer, IllegalArgumentException> operation = (a, b) -> {
            throw new IllegalArgumentException();
        };
        final ThrowableBiFunction<Integer, Integer, Integer, IllegalArgumentException> function = operation.andThen(
            andThenFunction);
        assertThrows(IllegalArgumentException.class, () -> function.apply(1, 2), "The operation should fail");
        assertEquals(0, indicator.get(), "AndThenFunction should not have executed since the operation failed");
    }

    private void throwException() {
        throw new IllegalArgumentException();
    }

    private void throwException2() throws Exception {
        throw new Exception();
    }
}
