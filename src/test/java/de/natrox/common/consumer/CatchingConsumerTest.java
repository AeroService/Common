package de.natrox.common.consumer;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CatchingConsumerTest {

    @Test
    void defaultApplyTest() {
        AtomicInteger indicator = new AtomicInteger();
        CatchingConsumer<Integer> consumer = new CatchingConsumer<>((a) -> indicator.set(value(a)));
        consumer.accept(1);
        assertEquals(1, indicator.get(), "Consumer should have performed actions correctly.");
        consumer.accept(2);
        assertEquals(2, indicator.get(), "Consumer should have performed actions correctly.");
    }

    @Test
    void exceptionApplyTest() {
        CatchingConsumer<Integer> consumer = new CatchingConsumer<>(this::value);
        assertThrows(IllegalArgumentException.class, () ->
            consumer.accept(-1), "Consumer should throw an exception if the arguments do not meet the conditions.");
    }

    private int value(int a) {
        if (a <= 0)
            throw new IllegalArgumentException();
        return a;
    }
}
