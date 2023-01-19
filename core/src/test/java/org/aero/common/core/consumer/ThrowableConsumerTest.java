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

class ThrowableConsumerTest {

    @Test
    void testAccept() {
        final AtomicInteger indicator = new AtomicInteger();
        final ThrowableConsumer<Integer, IllegalArgumentException> consumer = indicator::set;
        consumer.accept(1);
        assertEquals(1, indicator.get(), "Indicator should indicate the input of 1");
        consumer.accept(2);
        assertEquals(2, indicator.get(), "Indicator should indicate the input of 2");
    }

    @Test
    void testThrowingAccept() {
        final ThrowableConsumer<Integer, IllegalArgumentException> consumer = i -> this.throwException();
        assertThrows(IllegalArgumentException.class, () -> consumer.accept(-1),
            "Consumer should throw an exception if the arguments don't meet the conditions");
    }

    @Test
    void testThrowingAccept2() {
        final ThrowableConsumer<Integer, Exception> consumer = i -> this.throwException2();
        assertThrows(Exception.class, () -> consumer.accept(-1),
            "Consumer should throw an exception if the arguments don't meet the conditions");
    }

    @Test
    void testAndThenAccept() {
        final AtomicInteger indicator = new AtomicInteger();
        final ThrowableConsumer<Integer, IllegalArgumentException> andThenConsumer = (a) -> indicator.addAndGet(-a);
        final ThrowableConsumer<Integer, IllegalArgumentException> operation = indicator::set;
        final ThrowableConsumer<Integer, IllegalArgumentException> consumer = operation.andThen(andThenConsumer);
        consumer.accept(1);
        assertEquals(0, indicator.get(), "Indicator should indicate the input minus the input, which equals zero");
        consumer.accept(2);
        assertEquals(0, indicator.get(), "Indicator should indicate the input minus the input, which equals zero");
    }

    @Test
    void testAndThenNull() {
        final ThrowableConsumer<Integer, IllegalArgumentException> consumer = i -> this.throwException();
        assertThrows(IllegalArgumentException.class, () ->
            consumer.andThen(null), "Consumer should throw a IllegalArgumentException if the andThen consumer is invalid");
    }

    @Test
    void testAndThenExecution() {
        final AtomicInteger indicator = new AtomicInteger();
        final ThrowableConsumer<Integer, IllegalArgumentException> andThenConsumer = (a) -> indicator.incrementAndGet();
        final ThrowableConsumer<Integer, IllegalArgumentException> operation = (a) -> {
            throw new IllegalArgumentException();
        };
        final ThrowableConsumer<Integer, IllegalArgumentException> consumer = operation.andThen(andThenConsumer);
        assertThrows(IllegalArgumentException.class, () -> consumer.accept(1), "The operation should fail");
        assertEquals(0, indicator.get(), "AndThenConsumer should not have executed since the operation failed");
    }

    private void throwException() {
        throw new IllegalArgumentException();
    }

    private void throwException2() throws Exception {
        throw new Exception();
    }
}
