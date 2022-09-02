/*
 * Copyright 2020-2022 NatroxMC
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
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

class ThrowableFunctionTest {

    @Test
    void defaultApplyTest1() {
        ThrowableFunction<Integer, Integer, IllegalArgumentException> function = this::value;
        assertEquals(1, function.apply(1), "Function should return the input of 1");
        assertEquals(2, function.apply(2), "Function should return the input of 2");
    }

    @Test
    void defaultApplyTest2() {
        ThrowableFunction<Integer, Integer, Exception> function = this::exceptionValue;
        try {
            assertEquals(1, function.apply(1), "Function should return the input of 1");
            assertEquals(2, function.apply(2), "Function should return the input of 2");
        } catch (Exception e) {
            fail("Function should not throw an exception as the arguments are valid");
        }
    }

    @Test
    void exceptionApplyTest1() {
        ThrowableFunction<Integer, Integer, IllegalArgumentException> function = this::value;
        assertThrows(IllegalArgumentException.class, () ->
            function.apply(-1), "Function should throw an exception if the arguments don't meet the conditions");
    }

    @Test
    void exceptionApplyTest2() {
        ThrowableFunction<Integer, Integer, Exception> function = this::exceptionValue;
        assertThrows(Exception.class, () ->
            function.apply(-1), "Function should throw an exception if the arguments don't meet the conditions");
    }

    @Test
    void andThenApplyTest() {
        Function<Integer, Integer> andThenFunction = (a) -> (0);
        ThrowableFunction<Integer, Integer, IllegalArgumentException> operation = this::value;
        ThrowableFunction<Integer, Integer, IllegalArgumentException> function = operation.andThen(andThenFunction);
        assertEquals(0, function.apply(1), "Function should return zero");
        assertEquals(0, function.apply(2), "Function should return zero");
    }

    @Test
    void andThenNullTest() {
        ThrowableFunction<Integer, Integer, IllegalArgumentException> function = this::value;
        assertThrows(NullPointerException.class, () ->
            function.andThen(null), "Function should throw a NullPointerException if the andThen function is invalid");
    }

    @Test
    void andThenExecutionTest() {
        AtomicInteger indicator = new AtomicInteger();
        Function<Integer, Integer> andThenFunction = (a) -> indicator.incrementAndGet();
        ThrowableFunction<Integer, Integer, IllegalArgumentException> operation = (a) -> {
            throw new IllegalArgumentException();
        };
        ThrowableFunction<Integer, Integer, IllegalArgumentException> function = operation.andThen(andThenFunction);
        assertThrows(IllegalArgumentException.class, () -> function.apply(1), "The operation should fail");
        assertEquals(0, indicator.get(), "AndThenFunction should not have executed since the operation failed");
    }

    private int value(int a) {
        if (a <= 0) {
            throw new IllegalArgumentException();
        }
        return a;
    }

    private int exceptionValue(int a) throws Exception {
        if (a <= 0) {
            throw new Exception();
        }
        return a;
    }
}
