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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QuadConsumerTest {

    @Test
    void testAccept() {
        final AtomicInteger indicator = new AtomicInteger();
        final QuadConsumer<Integer, Integer, Integer, Integer> consumer = (a, b, c, d) -> indicator.set(this.sum(a, b, c, d));
        consumer.accept(1, 2, 3, 4);
        assertEquals(10, indicator.get(), "Indicator should indicate the input sum of 10");
        consumer.accept(5, 4, 3, 2);
        assertEquals(14, indicator.get(), "Indicator should indicate the input sum of 14");
    }

    @Test
    void testNullAccept() {
        final QuadConsumer<Integer, Integer, Integer, Integer> consumer = this::sum;
        assertThrows(NullPointerException.class, () ->
                consumer.accept(null, null, null, null),
            "Consumer should throw a NullPointerException if the arguments are null");
    }

    @Test
    void testAndThenAccept() {
        final AtomicInteger indicator = new AtomicInteger();
        final QuadConsumer<Integer, Integer, Integer, Integer> andThenConsumer = (a, b, c, d) -> indicator.addAndGet(
            -this.sum(a, b, c, d));
        final QuadConsumer<Integer, Integer, Integer, Integer> operation = (a, b, c, d) -> indicator.set(
            this.sum(a, b, c, d));
        final QuadConsumer<Integer, Integer, Integer, Integer> consumer = operation.andThen(andThenConsumer);
        consumer.accept(1, 2, 3, 4);
        assertEquals(0, indicator.get(),
            "Indicator should indicate the input sum minus the input sum, which equals zero");
        consumer.accept(5, 4, 3, 2);
        assertEquals(0, indicator.get(),
            "Indicator should indicate the input sum minus the input sum, which equals zero");
    }

    @Test
    void testAndThenNull() {
        final QuadConsumer<Integer, Integer, Integer, Integer> consumer = this::sum;
        assertThrows(IllegalArgumentException.class, () -> consumer.andThen(null),
            "Consumer should throw a IllegalArgumentException if the andThen consumer is invalid");
    }

    @Test
    void testAndThenExecution() {
        final AtomicInteger indicator = new AtomicInteger();
        final QuadConsumer<Integer, Integer, Integer, Integer> andThenConsumer = (a, b, c, d) -> indicator.incrementAndGet();
        final QuadConsumer<Integer, Integer, Integer, Integer> operation = (a, b, c, d) -> {
            throw new IllegalArgumentException();
        };
        final QuadConsumer<Integer, Integer, Integer, Integer> consumer = operation.andThen(andThenConsumer);
        assertThrows(IllegalArgumentException.class, () -> consumer.accept(1, 2, 3, 4), "The operation should fail");
        assertEquals(0, indicator.get(), "AndThenConsumer should not have executed since the operation failed");
    }

    private int sum(final int a, final int b, final int c, final int d) {
        return a + b + c + d;
    }
}
