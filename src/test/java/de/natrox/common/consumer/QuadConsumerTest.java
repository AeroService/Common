package de.natrox.common.consumer;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QuadConsumerTest {

    @Test
    void defaultApplyTest() {
        AtomicInteger indicator = new AtomicInteger();
        QuadConsumer<Integer, Integer, Integer, Integer> consumer = (a, b, c, d) -> indicator.set(this.addition(a, b, c, d));
        consumer.accept(1, 2, 3, 4);
        assertEquals(10, indicator.get(), "Indicator should indicate the input sum of 10.");
        consumer.accept(5, 4, 3, 2);
        assertEquals(14, indicator.get(), "Indicator should indicate the input sum of 14.");
    }

    @Test
    void nullApplyTest() {
        QuadConsumer<Integer, Integer, Integer, Integer> consumer = this::addition;
        assertThrows(NullPointerException.class, () ->
            consumer.accept(null, null, null, null), "Consumer should throw a NullPointerException if the arguments are null.");
    }

    private int addition(int a, int b, int c, int d) {
        return a + b + c + d;
    }
}
