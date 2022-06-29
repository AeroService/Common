package de.natrox.common.consumer;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CatchingConsumerTest {

    @Test
    void defaultAcceptTest() {
        AtomicInteger indicator = new AtomicInteger();
        CatchingConsumer<Integer> consumer = new CatchingConsumer<>((a) -> indicator.set(value(a)));
        consumer.accept(1);
        assertEquals(1, indicator.get(), "Indicator should indicate the input of 1.");
        consumer.accept(2);
        assertEquals(2, indicator.get(), "Indicator should indicate the input of 2.");
    }

    @Test
    void exceptionAcceptTest() {
        CatchingConsumer<Integer> consumer = new CatchingConsumer<>(this::value);
        assertThrows(IllegalArgumentException.class, () ->
            consumer.accept(-1), "Consumer should throw an exception if the arguments don't meet the conditions.");
    }

    private int value(int a) {
        if (a <= 0)
            throw new IllegalArgumentException();
        return a;
    }
}
