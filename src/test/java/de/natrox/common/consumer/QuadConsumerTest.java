package de.natrox.common.consumer;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QuadConsumerTest {

    @Test
    void defaultAcceptTest() {
        AtomicInteger indicator = new AtomicInteger();
        QuadConsumer<Integer, Integer, Integer, Integer> consumer = (a, b, c, d) -> indicator.set(this.sum(a, b, c, d));
        consumer.accept(1, 2, 3, 4);
        assertEquals(10, indicator.get(), "Indicator should indicate the input sum of 10.");
        consumer.accept(5, 4, 3, 2);
        assertEquals(14, indicator.get(), "Indicator should indicate the input sum of 14.");
    }

    @Test
    void nullAcceptTest() {
        QuadConsumer<Integer, Integer, Integer, Integer> consumer = this::sum;
        assertThrows(NullPointerException.class, () ->
            consumer.accept(null, null, null, null), "Consumer should throw a NullPointerException if the arguments are null.");
    }

    @Test
    void andThenAcceptTest() {
        AtomicInteger indicator = new AtomicInteger();
        QuadConsumer<Integer, Integer, Integer, Integer> andThenConsumer = (a, b, c, d) -> indicator.addAndGet(-this.sum(a, b, c, d));
        QuadConsumer<Integer, Integer, Integer, Integer> operation = (a, b, c, d) -> indicator.set(this.sum(a, b, c, d));
        QuadConsumer<Integer, Integer, Integer, Integer> consumer = operation.andThen(andThenConsumer);
        consumer.accept(1, 2, 3, 4);
        assertEquals(0, indicator.get(), "Indicator should indicate the input sum minus the input sum, which equals zero.");
        consumer.accept(5, 4, 3, 2);
        assertEquals(0, indicator.get(), "Indicator should indicate the input sum minus the input sum, which equals zero.");
    }

    @Test
    void andThenNullTest() {
        QuadConsumer<Integer, Integer, Integer, Integer> consumer = this::sum;
        assertThrows(NullPointerException.class, () ->
            consumer.andThen(null), "Consumer should throw a NullPointerException if the andThen consumer is invalid.");
    }

    @Test
    void andThenExecutionTest() {
        AtomicInteger indicator = new AtomicInteger();
        QuadConsumer<Integer, Integer, Integer, Integer> andThenConsumer = (a, b, c, d) -> indicator.incrementAndGet();
        QuadConsumer<Integer, Integer, Integer, Integer> operation = (a, b, c, d) -> {
            throw new IllegalArgumentException();
        };
        QuadConsumer<Integer, Integer, Integer, Integer> consumer = operation.andThen(andThenConsumer);
        assertThrows(IllegalArgumentException.class, () -> consumer.accept(1, 2, 3, 4), "The operation should fail.");
        assertEquals(0, indicator.get(), "AndThenConsumer should not have executed since the operation failed.");
    }

    private int sum(int a, int b, int c, int d) {
        return a + b + c + d;
    }
}
