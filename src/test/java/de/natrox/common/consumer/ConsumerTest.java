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
