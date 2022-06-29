package de.natrox.common.function;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QuadFunctionTest {

    @Test
    void defaultApplyTest() {
        QuadFunction<Integer, Integer, Integer, Integer, Integer> function = this::sum;
        assertEquals(10, function.apply(1, 2, 3, 4), "Function should return the input sum of 10.");
        assertEquals(14, function.apply(5, 4, 3, 2), "Function should return the input sum of 14.");
    }

    @Test
    void nullApplyTest() {
        QuadFunction<Integer, Integer, Integer, Integer, Integer> function = this::sum;
        assertThrows(NullPointerException.class, () ->
            function.apply(null, null, null, null), "Function should throw a NullPointerException if the arguments are null.");
    }

    @Test
    void andThenApplyTest() {
        Function<Integer, Integer> andThenFunction = (a) -> (0);
        QuadFunction<Integer, Integer, Integer, Integer, Integer> operation = this::sum;
        QuadFunction<Integer, Integer, Integer, Integer, Integer> function = operation.andThen(andThenFunction);
        assertEquals(0, function.apply(1, 2, 3, 4), "Function should return zero.");
        assertEquals(0, function.apply(5, 4, 3, 2), "Function should return zero.");
    }

    @Test
    void andThenNullTest() {
        QuadFunction<Integer, Integer, Integer, Integer, Integer> function = this::sum;
        assertThrows(NullPointerException.class, () ->
            function.andThen(null), "Function should throw a NullPointerException if the andThen function is invalid.");
    }

    @Test
    void andThenExecutionTest() {
        AtomicInteger indicator = new AtomicInteger();
        Function<Integer, Integer> andThenFunction = (a) -> (indicator.incrementAndGet());
        QuadFunction<Integer, Integer, Integer, Integer, Integer> operation = (a, b, c, d) -> {
            throw new IllegalArgumentException();
        };
        QuadFunction<Integer, Integer, Integer, Integer, Integer> function = operation.andThen(andThenFunction);
        assertThrows(IllegalArgumentException.class, () -> function.apply(1, 2, 3, 4), "The operation should fail.");
        assertEquals(0, indicator.get(), "AndThenFunction should not have executed since the operation failed.");
    }

    private int sum(int a, int b, int c, int d) {
        return a + b + c + d;
    }
}
