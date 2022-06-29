package de.natrox.common.function;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class ThrowableBiFunctionTest {

    @Test
    void defaultApplyTest1() {
        ThrowableBiFunction<Integer, Integer, Integer, IllegalArgumentException> function = this::sum;
        assertEquals(3, function.apply(1, 2), "Function should return the input sum of 3.");
        assertEquals(5, function.apply(2, 3), "Function should return the input sum of 5.");
    }

    @Test
    void defaultApplyTest2() {
        ThrowableBiFunction<Integer, Integer, Integer, Exception> function = this::exceptionSum;
        try {
            assertEquals(3, function.apply(1, 2), "Function should return the input sum of 3.");
            assertEquals(5, function.apply(2, 3), "Function should return the input sum of 5.");
        } catch (Exception e) {
            fail("Function should not throw an exception as the arguments are valid.");
        }
    }

    @Test
    void exceptionApplyTest1() {
        ThrowableBiFunction<Integer, Integer, Integer, IllegalArgumentException> function = this::sum;
        assertThrows(IllegalArgumentException.class, () ->
            function.apply(-5, 3), "Function should throw an exception if the arguments don't meet the conditions.");
    }

    @Test
    void exceptionApplyTest2() {
        ThrowableBiFunction<Integer, Integer, Integer, Exception> function = this::exceptionSum;
        assertThrows(Exception.class, () ->
            function.apply(-5, 3), "Function should throw an exception if the arguments don't meet the conditions.");
    }

    @Test
    void andThenApplyTest() {
        Function<Integer, Integer> andThenFunction = (a) -> (0);
        ThrowableBiFunction<Integer, Integer, Integer, IllegalArgumentException> operation = this::sum;
        ThrowableBiFunction<Integer, Integer, Integer, IllegalArgumentException> function = operation.andThen(andThenFunction);
        assertEquals(0, function.apply(1, 2), "Function should return zero.");
        assertEquals(0, function.apply(2, 3), "Function should return zero.");
    }

    @Test
    void andThenNullTest() {
        ThrowableBiFunction<Integer, Integer, Integer, IllegalArgumentException> function = this::sum;
        assertThrows(NullPointerException.class, () ->
            function.andThen(null), "Function should throw a NullPointerException if the andThen function is invalid.");
    }

    @Test
    void andThenExecutionTest() {
        AtomicInteger indicator = new AtomicInteger();
        Function<Integer, Integer> andThenFunction = (a) -> indicator.incrementAndGet();
        ThrowableBiFunction<Integer, Integer, Integer, IllegalArgumentException> operation = (a, b) -> {
            throw new IllegalArgumentException();
        };
        ThrowableBiFunction<Integer, Integer, Integer, IllegalArgumentException> function = operation.andThen(andThenFunction);
        assertThrows(IllegalArgumentException.class, () -> function.apply(1, 2), "The operation should fail.");
        assertEquals(0, indicator.get(), "AndThenFunction should not have executed since the operation failed.");
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
