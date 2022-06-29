package de.natrox.common.function;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CatchingFunctionTest {

    @Test
    void defaultApplyTest() {
        CatchingFunction<Integer, Integer> function = new CatchingFunction<>(this::value);
        assertEquals(1, function.apply(1), "Function should return the input of 1.");
        assertEquals(2, function.apply(2), "Function should return the input of 2.");
    }

    @Test
    void exceptionApplyTest() {
        CatchingFunction<Integer, Integer> function = new CatchingFunction<>(this::value);
        assertThrows(IllegalArgumentException.class, () ->
            function.apply(-1), "Function should throw an exception if the arguments don't meet the conditions.");
    }

    private int value(int a) {
        if (a <= 0)
            throw new IllegalArgumentException();
        return a;
    }
}
