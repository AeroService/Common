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

package org.aero.common.core.consumer;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ThrowableBiConsumerTest {

    @Test
    void testAccept() {
        final AtomicInteger indicator = new AtomicInteger();
        final ThrowableBiConsumer<Integer, Integer, IllegalArgumentException> consumer = (a, b) -> indicator.set(a + b);
        consumer.accept(1, 2);
        assertEquals(3, indicator.get(), "Indicator should indicate the input sum of 3");
        consumer.accept(2, 3);
        assertEquals(5, indicator.get(), "Indicator should indicate the input sum of 5");
    }

    @Test
    void testAccept2() {
        final AtomicInteger indicator = new AtomicInteger();
        final ThrowableBiConsumer<Integer, Integer, Exception> consumer = (a, b) -> indicator.set(a + b);
        assertDoesNotThrow(() -> consumer.accept(1, 2),
            "Consumer should not throw an exception as the arguments are valid");
        assertEquals(3, indicator.get(), "Indicator should indicate the input sum of 3");
        assertDoesNotThrow(() -> consumer.accept(2, 3),
            "Consumer should not throw an exception as the arguments are valid");
        assertEquals(5, indicator.get(), "Indicator should indicate the input sum of 5");
    }

    @Test
    void testThrowingAccept() {
        final ThrowableBiConsumer<Integer, Integer, IllegalArgumentException> consumer = (i, j) -> this.throwException();
        assertThrows(IllegalArgumentException.class, () ->
            consumer.accept(-5, 3), "Consumer should throw an exception if the arguments don't meet the conditions");
    }

    @Test
    void testThrowingAccept2() {
        final ThrowableBiConsumer<Integer, Integer, Exception> consumer = (i, j) -> this.throwException2();
        assertThrows(Exception.class, () ->
            consumer.accept(-5, 3), "Consumer should throw an exception if the arguments don't meet the conditions");
    }

    @Test
    void testAndThenAccept() {
        final AtomicInteger indicator = new AtomicInteger();
        final ThrowableBiConsumer<Integer, Integer, IllegalArgumentException> andThenConsumer = (a, b) -> indicator.addAndGet(-(a + b));
        final ThrowableBiConsumer<Integer, Integer, IllegalArgumentException> operation = (a, b) -> indicator.set(a + b);
        final ThrowableBiConsumer<Integer, Integer, IllegalArgumentException> consumer = operation.andThen(andThenConsumer);
        consumer.accept(1, 2);
        assertEquals(0, indicator.get(),
            "Indicator should indicate the input sum minus the input sum, which equals zero");
        consumer.accept(3, 2);
        assertEquals(0, indicator.get(),
            "Indicator should indicate the input sum minus the input sum, which equals zero");
    }

    @Test
    void testAndThenNull() {
        final ThrowableBiConsumer<Integer, Integer, IllegalArgumentException> consumer = (i, j) -> this.throwException();
        assertThrows(IllegalArgumentException.class, () ->
            consumer.andThen(null), "Consumer should throw a IllegalArgumentException if the andThen consumer is invalid");
    }

    @Test
    void testAndThenExecution() {
        final AtomicInteger indicator = new AtomicInteger();
        final ThrowableBiConsumer<Integer, Integer, IllegalArgumentException> andThenConsumer = (a, b) -> indicator.incrementAndGet();
        final ThrowableBiConsumer<Integer, Integer, IllegalArgumentException> operation = (a, b) -> {
            throw new IllegalArgumentException();
        };
        final ThrowableBiConsumer<Integer, Integer, IllegalArgumentException> consumer = operation.andThen(andThenConsumer);
        assertThrows(IllegalArgumentException.class, () -> consumer.accept(1, 2), "The operation should fail");
        assertEquals(0, indicator.get(), "AndThenConsumer should not have executed since the operation failed");
    }

    private void throwException() {
        throw new IllegalArgumentException();
    }

    private void throwException2() throws Exception {
        throw new Exception();
    }
}
