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
        AtomicLong l = new AtomicLong();
        TriConsumer<Short, Integer, Long> consumer = (aShort, aInt, aLong) -> l.set(aShort + aInt + aLong);
        consumer.accept((short) 3, 20, 100L);
        assertEquals(123, l.get());
    }

    @Test
    void quadConsumerTest() {
        AtomicLong l = new AtomicLong();
        QuadConsumer<Byte, Short, Integer, Long> consumer = (aByte, aShort, aInt, aLong) -> l.set(aByte + aShort + aInt + aLong);
        consumer.accept((byte) 4, (short) 30, 200, 1000L);
        assertEquals(1234, l.get());
    }

    @Test
    void throwableSupplierTest() {
        ThrowableConsumer<String, RuntimeException> consumer = this::stringConsumer;
        assertThrows(RuntimeException.class, () -> consumer.accept("foo"));
    }

    @Test
    void catchingSupplierTest() {
        CatchingConsumer<String> consumer = new CatchingConsumer<>(this::stringConsumer);
        assertThrows(RuntimeException.class, () -> consumer.accept("foo"));
    }

    void stringConsumer(String s) throws RuntimeException {
        throw new RuntimeException();
    }
}
