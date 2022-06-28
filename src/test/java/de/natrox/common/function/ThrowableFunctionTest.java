package de.natrox.common.function;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ThrowableFunctionTest {

    @Test
    void defaultApplyTest1() {
        ThrowableFunction<String, Character, IllegalArgumentException> function = this::firstChar;
        assertEquals('f', function.apply("foo"), "Function should return 'f' as the first char of \"foo\".");
        assertEquals('b', function.apply("boo"),"Function should return 'b' as the first char of \"boo\".");
    }

    @Test
    void defaultApplyTest2() {
        ThrowableFunction<Double, Double, StringIndexOutOfBoundsException> function = this::inverse;
        assertEquals(1D / 2, function.apply(2D), "Function should return .5 as the multiplicative inverse of 2.");
        assertEquals(2D, function.apply(1D / 2), "Function should return 2 as the multiplicative inverse of .5.");
    }

    @Test
    void defaultApplyTest3() {
        try {
            ThrowableFunction<Integer, Double, Exception> function = this::sqrt;
            assertEquals(4.0, function.apply(16), "Function should return 4 as the square root of 16.");
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void exceptionApplyTest1() {
        ThrowableFunction<String, Character, StringIndexOutOfBoundsException> function = this::firstChar;
        assertThrows(StringIndexOutOfBoundsException.class, () -> function.apply(""), "Function should throw an exception if the arguments do not meet the preset conditions.");
    }

    @Test
    void exceptionApplyTest2() {
        ThrowableFunction<Double, Double, IllegalArgumentException> function = this::inverse;
        assertThrows(IllegalArgumentException.class, () -> function.apply(0D), "Function should throw an exception if the arguments do not meet the preset conditions.");
    }

    @Test
    void exceptionApplyTest3() {
        ThrowableFunction<Integer, Double, Exception> function = this::sqrt;
        assertThrows(Exception.class, () -> function.apply(-1), "Function should throw an exception if the arguments do not meet the preset conditions.");
    }

    char firstChar(String s) {
        return s.charAt(0);
    }

    double inverse(double a) {
        if (a == 0)
            throw new IllegalArgumentException();
        return 1 / a;
    }

    double sqrt(int a) throws Exception {
        if (a < 0)
            throw new Exception();
        return Math.sqrt(a);
    }
}
