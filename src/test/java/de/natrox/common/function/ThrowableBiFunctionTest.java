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

package de.natrox.common.function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import org.junit.jupiter.api.Test;

class ThrowableBiFunctionTest {

    @Test
    void testApply() {
        ThrowableBiFunction<Integer, Integer, Integer, IllegalArgumentException> function = this::sum;
        assertEquals(3, function.apply(1, 2), "Function should return the input sum of 3");
        assertEquals(5, function.apply(2, 3), "Function should return the input sum of 5");
    }

    @Test
    void testApply2() {
        ThrowableBiFunction<Integer, Integer, Integer, Exception> function = this::exceptionSum;
        try {
            assertEquals(3, function.apply(1, 2), "Function should return the input sum of 3");
            assertEquals(5, function.apply(2, 3), "Function should return the input sum of 5");
        } catch (Exception e) {
            fail("Function should not throw an exception as the arguments are valid");
        }
    }

    @Test
    void testThrowingApply() {
        ThrowableBiFunction<Integer, Integer, Integer, IllegalArgumentException> function = this::sum;
        assertThrows(IllegalArgumentException.class, () ->
            function.apply(-5, 3), "Function should throw an exception if the arguments don't meet the conditions");
    }

    @Test
    void testThrowingApply2() {
        ThrowableBiFunction<Integer, Integer, Integer, Exception> function = this::exceptionSum;
        assertThrows(Exception.class, () ->
            function.apply(-5, 3), "Function should throw an exception if the arguments don't meet the conditions");
    }

    @Test
    void testAndThenApply() {
        Function<Integer, Integer> andThenFunction = (a) -> (0);
        ThrowableBiFunction<Integer, Integer, Integer, IllegalArgumentException> operation = this::sum;
        ThrowableBiFunction<Integer, Integer, Integer, IllegalArgumentException> function = operation.andThen(
            andThenFunction);
        assertEquals(0, function.apply(1, 2), "Function should return zero");
        assertEquals(0, function.apply(2, 3), "Function should return zero");
    }

    @Test
    void testAndThenNull() {
        ThrowableBiFunction<Integer, Integer, Integer, IllegalArgumentException> function = this::sum;
        assertThrows(NullPointerException.class, () ->
            function.andThen(null), "Function should throw a NullPointerException if the andThen function is invalid");
    }

    @Test
    void testAndThenExecution() {
        AtomicInteger indicator = new AtomicInteger();
        Function<Integer, Integer> andThenFunction = (a) -> indicator.incrementAndGet();
        ThrowableBiFunction<Integer, Integer, Integer, IllegalArgumentException> operation = (a, b) -> {
            throw new IllegalArgumentException();
        };
        ThrowableBiFunction<Integer, Integer, Integer, IllegalArgumentException> function = operation.andThen(
            andThenFunction);
        assertThrows(IllegalArgumentException.class, () -> function.apply(1, 2), "The operation should fail");
        assertEquals(0, indicator.get(), "AndThenFunction should not have executed since the operation failed");
    }

    private int sum(int a, int b) {
        if (a + b <= 0) {
            throw new IllegalArgumentException();
        }
        return a + b;
    }

    private int exceptionSum(int a, int b) throws Exception {
        if (a + b <= 0) {
            throw new Exception();
        }
        return a + b;
    }
}
