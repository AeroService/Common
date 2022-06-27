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

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConsumerTest {

    @Test
    void triConsumerTest() {
        AtomicLong indicator = new AtomicLong();
        TriConsumer<Short, Integer, Long> consumer = (first, second, third) -> indicator.set(first + second + third);
        consumer.accept((short) 3, 20, 100L);
        assertEquals(123, indicator.get());
    }

    @Test
    void quadConsumerTest() {
        AtomicLong indicator = new AtomicLong();
        QuadConsumer<Byte, Short, Integer, Long> consumer = (first, second, third, fourth) -> indicator.set(first + second + third + fourth);
        consumer.accept((byte) 4, (short) 30, 200, 1000L);
        assertEquals(1234, indicator.get());
    }

    @Test
    void throwableConsumerTest() {
        ThrowableConsumer<String, RuntimeException> consumer = this::throwRuntimeException;
        assertThrows(RuntimeException.class, () -> consumer.accept("foo"));
    }

    @Test
    void throwableBiConsumerTest() {
        ThrowableBiConsumer<String, String, RuntimeException> consumer = this::throwRuntimeException;
        assertThrows(RuntimeException.class, () -> consumer.accept("foo", "foo"));
    }

    @Test
    void catchingConsumerTest() {
        CatchingConsumer<String> consumer = new CatchingConsumer<>(this::throwRuntimeException);
        assertThrows(RuntimeException.class, () -> consumer.accept("foo"));
    }

    void throwRuntimeException(String s) {
        throw new RuntimeException();
    }

    void throwRuntimeException(String s, String s2) {
        throw new RuntimeException();
    }
}
