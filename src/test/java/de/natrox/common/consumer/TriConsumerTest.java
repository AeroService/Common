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

package de.natrox.common.consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class TriConsumerTest {

    @Test
    void defaultAcceptTest() {
        AtomicInteger indicator = new AtomicInteger();
        TriConsumer<Integer, Integer, Integer> consumer = (a, b, c) -> indicator.set(this.sum(a, b, c));
        consumer.accept(1, 2, 3);
        assertEquals(6, indicator.get(), "Indicator should indicate the input sum of 6");
        consumer.accept(4, 3, 2);
        assertEquals(9, indicator.get(), "Indicator should indicate the input sum of 9");
    }

    @Test
    void nullAcceptTest() {
        TriConsumer<Integer, Integer, Integer> consumer = this::sum;
        assertThrows(NullPointerException.class, () ->
                consumer.accept(null, null, null),
            "Consumer should throw a NullPointerException if the arguments are null");
    }

    @Test
    void andThenAcceptTest() {
        AtomicInteger indicator = new AtomicInteger();
        TriConsumer<Integer, Integer, Integer> andThenConsumer = (a, b, c) -> indicator.addAndGet(-this.sum(a, b, c));
        TriConsumer<Integer, Integer, Integer> operation = (a, b, c) -> indicator.set(this.sum(a, b, c));
        TriConsumer<Integer, Integer, Integer> consumer = operation.andThen(andThenConsumer);
        consumer.accept(1, 2, 3);
        assertEquals(0, indicator.get(),
            "Indicator should indicate the input sum minus the input sum, which equals zero");
        consumer.accept(4, 3, 2);
        assertEquals(0, indicator.get(),
            "Indicator should indicate the input sum minus the input sum, which equals zero");
    }

    @Test
    void andThenNullTest() {
        TriConsumer<Integer, Integer, Integer> consumer = this::sum;
        assertThrows(NullPointerException.class, () ->
            consumer.andThen(null), "Consumer should throw a NullPointerException if the andThen consumer is invalid");
    }

    @Test
    void andThenExecutionTest() {
        AtomicInteger indicator = new AtomicInteger();
        TriConsumer<Integer, Integer, Integer> andThenConsumer = (a, b, c) -> indicator.incrementAndGet();
        TriConsumer<Integer, Integer, Integer> operation = (a, b, c) -> {
            throw new IllegalArgumentException();
        };
        TriConsumer<Integer, Integer, Integer> consumer = operation.andThen(andThenConsumer);
        assertThrows(IllegalArgumentException.class, () -> consumer.accept(1, 2, 3), "The operation should fail");
        assertEquals(0, indicator.get(), "AndThenConsumer should not have executed since the operation failed");
    }

    private int sum(int a, int b, int c) {
        return a + b + c;
    }
}
