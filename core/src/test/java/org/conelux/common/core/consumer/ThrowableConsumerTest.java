/*
 * Copyright 2020-2022 Conelux
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

package org.conelux.common.core.consumer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class ThrowableConsumerTest {

    @Test
    void testAccept() {
        AtomicInteger indicator = new AtomicInteger();
        ThrowableConsumer<Integer, IllegalArgumentException> consumer = (a) -> indicator.set(value(a));
        consumer.accept(1);
        assertEquals(1, indicator.get(), "Indicator should indicate the input of 1");
        consumer.accept(2);
        assertEquals(2, indicator.get(), "Indicator should indicate the input of 2");
    }

    @Test
    void testAccept2() {
        AtomicInteger indicator = new AtomicInteger();
        ThrowableConsumer<Integer, Exception> consumer = (a) -> indicator.set(exceptionValue(a));
        assertDoesNotThrow(() -> consumer.accept(1),
            "Consumer should not throw an exception as the arguments are valid");
        assertEquals(1, indicator.get(), "Indicator should indicate the input of 1");
        assertDoesNotThrow(() -> consumer.accept(2),
            "Consumer should not throw an exception as the arguments are valid");
        assertEquals(2, indicator.get(), "Indicator should indicate the input of 2");
    }

    @Test
    void testThrowingAccept() {
        ThrowableConsumer<Integer, IllegalArgumentException> consumer = this::value;
        assertThrows(IllegalArgumentException.class, () -> consumer.accept(-1),
            "Consumer should throw an exception if the arguments don't meet the conditions");
    }

    @Test
    void testThrowingAccept2() {
        ThrowableConsumer<Integer, Exception> consumer = this::exceptionValue;
        assertThrows(Exception.class, () -> consumer.accept(-1),
            "Consumer should throw an exception if the arguments don't meet the conditions");
    }

    @Test
    void testAndThenAccept() {
        AtomicInteger indicator = new AtomicInteger();
        ThrowableConsumer<Integer, IllegalArgumentException> andThenConsumer = (a) -> indicator.addAndGet(
            -this.value(a));
        ThrowableConsumer<Integer, IllegalArgumentException> operation = (a) -> indicator.set(this.value(a));
        ThrowableConsumer<Integer, IllegalArgumentException> consumer = operation.andThen(andThenConsumer);
        consumer.accept(1);
        assertEquals(0, indicator.get(), "Indicator should indicate the input minus the input, which equals zero");
        consumer.accept(2);
        assertEquals(0, indicator.get(), "Indicator should indicate the input minus the input, which equals zero");
    }

    @Test
    void testAndThenNull() {
        ThrowableConsumer<Integer, IllegalArgumentException> consumer = this::value;
        assertThrows(NullPointerException.class, () ->
            consumer.andThen(null), "Consumer should throw a NullPointerException if the andThen consumer is invalid");
    }

    @Test
    void testAndThenExecution() {
        AtomicInteger indicator = new AtomicInteger();
        ThrowableConsumer<Integer, IllegalArgumentException> andThenConsumer = (a) -> indicator.incrementAndGet();
        ThrowableConsumer<Integer, IllegalArgumentException> operation = (a) -> {
            throw new IllegalArgumentException();
        };
        ThrowableConsumer<Integer, IllegalArgumentException> consumer = operation.andThen(andThenConsumer);
        assertThrows(IllegalArgumentException.class, () -> consumer.accept(1), "The operation should fail");
        assertEquals(0, indicator.get(), "AndThenConsumer should not have executed since the operation failed");
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
