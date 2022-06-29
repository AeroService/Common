package de.natrox.common.consumer;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TriConsumerTest {

    @Test
    void defaultApplyTest() {
        AtomicInteger indicator = new AtomicInteger();
        TriConsumer<Integer, Integer, Integer> consumer = (a, b, c) -> indicator.set(this.addition(a, b, c));
        consumer.accept(1, 2, 3);
        assertEquals(6, indicator.get(), "Indicator should indicate the input sum of 6.");
        consumer.accept(4, 3, 2);
        assertEquals(9, indicator.get(), "Indicator should indicate the input sum of 9.");
    }

    @Test
    void nullApplyTest() {
        TriConsumer<Integer, Integer, Integer> consumer = this::addition;
        assertThrows(NullPointerException.class, () ->
            consumer.accept(null, null, null), "Consumer should throw a NullPointerException if the arguments are null.");
    }

    private int addition(int a, int b, int c) {
        return a + b + c;
    }
}
