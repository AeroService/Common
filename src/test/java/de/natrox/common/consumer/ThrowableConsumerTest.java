package de.natrox.common.consumer;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class ThrowableConsumerTest {

    @Test
    void defaultAcceptTest1() {
        AtomicInteger indicator = new AtomicInteger();
        ThrowableConsumer<Integer, IllegalArgumentException> consumer = (a) -> indicator.set(value(a));
        consumer.accept(1);
        assertEquals(1, indicator.get(), "Indicator should indicate the input of 1.");
        consumer.accept(2);
        assertEquals(2, indicator.get(), "Indicator should indicate the input of 2.");
    }

    @Test
    void defaultAcceptTest2() {
        AtomicInteger indicator = new AtomicInteger();
        ThrowableConsumer<Integer, Exception> consumer = (a) -> indicator.set(exceptionValue(a));
        assertDoesNotThrow(() -> consumer.accept(1), "Consumer should not throw an exception as the arguments are valid.");
        assertEquals(1, indicator.get(), "Indicator should indicate the input of 1.");
        assertDoesNotThrow(() -> consumer.accept(2), "Consumer should not throw an exception as the arguments are valid.");
        assertEquals(2, indicator.get(), "Indicator should indicate the input of 2.");
    }

    @Test
    void exceptionAcceptTest1() {
        ThrowableConsumer<Integer, IllegalArgumentException> consumer = this::value;
        assertThrows(IllegalArgumentException.class, () ->
            consumer.accept(-1), "Consumer should throw an exception if the arguments don't meet the conditions.");
    }

    @Test
    void exceptionAcceptTest2() {
        ThrowableConsumer<Integer, Exception> consumer = this::exceptionValue;
        assertThrows(Exception.class, () ->
            consumer.accept(-1), "Consumer should throw an exception if the arguments don't meet the conditions.");
    }

    @Test
    void andThenAcceptTest() {
        AtomicInteger indicator = new AtomicInteger();
        ThrowableConsumer<Integer, IllegalArgumentException> andThenConsumer = (a) -> indicator.addAndGet(-this.value(a));
        ThrowableConsumer<Integer, IllegalArgumentException> operation = (a) -> indicator.set(this.value(a));
        ThrowableConsumer<Integer, IllegalArgumentException> consumer = operation.andThen(andThenConsumer);
        consumer.accept(1);
        assertEquals(0, indicator.get(), "Indicator should indicate the input minus the input, which equals zero.");
        consumer.accept(2);
        assertEquals(0, indicator.get(), "Indicator should indicate the input minus the input, which equals zero.");
    }

    @Test
    void andThenNullTest() {
        ThrowableConsumer<Integer, IllegalArgumentException> consumer = this::value;
        assertThrows(NullPointerException.class, () ->
            consumer.andThen(null), "Consumer should throw a NullPointerException if the andThen consumer is invalid.");
    }

    @Test
    void andThenExecutionTest() {
        AtomicInteger indicator = new AtomicInteger();
        ThrowableConsumer<Integer, IllegalArgumentException> andThenConsumer = (a) -> indicator.incrementAndGet();
        ThrowableConsumer<Integer, IllegalArgumentException> operation = (a) -> {
            throw new IllegalArgumentException();
        };
        ThrowableConsumer<Integer, IllegalArgumentException> consumer = operation.andThen(andThenConsumer);
        assertThrows(IllegalArgumentException.class, () -> consumer.accept(1), "The operation should fail.");
        assertEquals(0, indicator.get(), "AndThenConsumer should not have executed since the operation failed.");
    }

    private int value(int a) {
        if (a <= 0)
            throw new IllegalArgumentException();
        return a;
    }

    private int exceptionValue(int a) throws Exception {
        if (a <= 0)
            throw new Exception();
        return a;
    }
}
