package de.natrox.common.function;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CatchingFunctionTest {

    @Test
    void defaultApplyTest1() {
        CatchingFunction<String, Character> function = new CatchingFunction<>(this::firstChar);
        assertEquals('f', function.apply("foo"), "Function should return 'f' as the first char of \"foo\".");
        assertEquals('b', function.apply("boo"), "Function should return 'b' as the first char of \"boo\".");
    }

    @Test
    void defaultApplyTest2() {
        CatchingFunction<Double, Double> function = new CatchingFunction<>(this::inverse);
        assertEquals(1D / 2, function.apply(2D), "Function should return .5 as the multiplicative inverse of 2.");
        assertEquals(2D, function.apply(1D / 2), "Function should return 2 as the multiplicative inverse of .5.");
    }

    @Test
    void exceptionApplyTest1() {
        CatchingFunction<String, Character> function = new CatchingFunction<>(this::firstChar);
        assertThrows(StringIndexOutOfBoundsException.class, () ->
            function.apply(""), "Function should throw an exception if the arguments do not meet the preset conditions.");
    }

    @Test
    void exceptionApplyTest2() {
        CatchingFunction<Double, Double> function = new CatchingFunction<>(this::inverse);
        assertThrows(IllegalArgumentException.class, () ->
            function.apply(0D), "Function should throw an exception if the arguments do not meet the preset conditions.");
    }

    char firstChar(String s) {
        return s.charAt(0);
    }

    double inverse(double a) {
        if (a == 0)
            throw new IllegalArgumentException();
        return 1 / a;
    }
}
