package de.natrox.common.consumer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ThrowableConsumerTest {

    private static char firstCharResult;
    private static double invertResult;
    private static double sqrtResult;

    @Test
    void defaultApplyTest1() {
        ThrowableConsumer<String, IllegalArgumentException> consumer = this::firstChar;
        consumer.accept("foo");
        assertEquals('f', firstCharResult, "Consumer should give expected output 'f' as the first char of \"foo\".");
        consumer.accept("boo");
        assertEquals('b', firstCharResult, "Consumer should give expected output 'b' as the first char of \"boo\".");
    }

    @Test
    void defaultApplyTest2() {
        ThrowableConsumer<Double, StringIndexOutOfBoundsException> consumer = this::inverse;
        consumer.accept(2D);
        assertEquals(1D / 2, invertResult, "Consumer should give expected output .5 as the multiplicative inverse of 2.");
        consumer.accept(1D / 2);
        assertEquals(2D, invertResult, "Consumer should give expected output 2 as the multiplicative inverse of .5.");
    }

    @Test
    void defaultApplyTest3() {
        try {
            ThrowableConsumer<Integer, Exception> consumer = this::sqrt;
            consumer.accept(16);
            assertEquals(4.0, sqrtResult, "Consumer should give expected output 4 as the square root of 16.");
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void exceptionApplyTest1() {
        ThrowableConsumer<String, StringIndexOutOfBoundsException> consumer = this::firstChar;
        assertThrows(StringIndexOutOfBoundsException.class, () ->
            consumer.accept(""), "Consumer should throw an exception if the arguments do not meet the preset conditions.");
    }

    @Test
    void exceptionApplyTest2() {
        ThrowableConsumer<Double, IllegalArgumentException> consumer = this::inverse;
        assertThrows(IllegalArgumentException.class, () ->
            consumer.accept(0D), "Consumer should throw an exception if the arguments do not meet the preset conditions.");
    }

    @Test
    void exceptionApplyTest3() {
        ThrowableConsumer<Integer, Exception> consumer = this::sqrt;
        assertThrows(Exception.class, () ->
            consumer.accept(-1), "Consumer should throw an exception if the arguments do not meet the preset conditions.");
    }

    void firstChar(String s) {
        firstCharResult = s.charAt(0);
    }

    void inverse(double a) {
        if (a == 0)
            throw new IllegalArgumentException();
        invertResult = 1 / a;
    }

    void sqrt(int a) throws Exception {
        if (a < 0)
            throw new Exception();
        sqrtResult = Math.sqrt(a);
    }
}
