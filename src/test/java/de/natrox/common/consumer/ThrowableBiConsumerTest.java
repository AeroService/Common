package de.natrox.common.consumer;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class ThrowableBiConsumerTest {

    @Test
    void defaultApplyTest1() {
        AtomicInteger indicator = new AtomicInteger();
        ThrowableBiConsumer<Integer, Integer, IllegalArgumentException> consumer = (a, b) -> indicator.set(sum(a, b));
        consumer.accept(1, 2);
        assertEquals(3, indicator.get(), "Indicator should indicate the input sum of 3.");
        consumer.accept(2, 3);
        assertEquals(5, indicator.get(), "Indicator should indicate the input sum of 5.");
    }

    @Test
    void defaultApplyTest2() {
        AtomicInteger indicator = new AtomicInteger();
        ThrowableBiConsumer<Integer, Integer, Exception> consumer = (a, b) -> indicator.set(exceptionSum(a, b));
        assertDoesNotThrow(() -> consumer.accept(1, 2), "Consumer should not throw an exception as the arguments are valid.");
        assertEquals(3, indicator.get(), "Indicator should indicate the input sum of 3.");
        assertDoesNotThrow(() -> consumer.accept(2, 3), "Consumer should not throw an exception as the arguments are valid.");
        assertEquals(5, indicator.get(), "Indicator should indicate the input sum of 5.");
    }

    @Test
    void exceptionApplyTest1() {
        ThrowableBiConsumer<Integer, Integer, IllegalArgumentException> consumer = this::sum;
        assertThrows(IllegalArgumentException.class, () ->
            consumer.accept(-5, 3), "Consumer should throw an exception if the arguments don't meet the conditions.");
    }

    @Test
    void exceptionApplyTest2() {
        ThrowableBiConsumer<Integer, Integer, Exception> consumer = this::exceptionSum;
        assertThrows(Exception.class, () ->
            consumer.accept(-5, 3), "Consumer should throw an exception if the arguments don't meet the conditions.");
    }

    private int sum(int a, int b) {
        if (a + b <= 0)
            throw new IllegalArgumentException();
        return a + b;
    }

    private int exceptionSum(int a, int b) throws Exception {
        if (a + b <= 0)
            throw new Exception();
        return a + b;
    }
}
