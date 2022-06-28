package de.natrox.common.consumer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CatchingConsumerTest {

    private static char firstCharResult;
    private static double invertResult;

    @Test
    void defaultApplyTest1() {
        CatchingConsumer<String> consumer = new CatchingConsumer<>(this::firstChar);
        consumer.accept("foo");
        assertEquals('f', firstCharResult, "Consumer should give expected output 'f' as the first char of \"foo\".");
        consumer.accept("boo");
        assertEquals('b', firstCharResult, "Consumer should give expected output 'b' as the first char of \"boo\".");
    }

    @Test
    void defaultApplyTest2() {
        CatchingConsumer<Double> consumer = new CatchingConsumer<>(this::inverse);
        consumer.accept(2D);
        assertEquals(1D / 2, invertResult, "Consumer should give expected output .5 as the multiplicative inverse of 2.");
        consumer.accept(1D / 2);
        assertEquals(2D, invertResult, "Consumer should give expected output 2 as the multiplicative inverse of .5.");
    }

    @Test
    void exceptionApplyTest1() {
        CatchingConsumer<String> consumer = new CatchingConsumer<>(this::firstChar);
        assertThrows(StringIndexOutOfBoundsException.class, () ->
            consumer.accept(""), "Consumer should throw an exception if the arguments do not meet the preset conditions.");
    }

    @Test
    void exceptionApplyTest2() {
        CatchingConsumer<Double> consumer = new CatchingConsumer<>(this::inverse);
        assertThrows(IllegalArgumentException.class, () ->
            consumer.accept(0D), "Consumer should throw an exception if the arguments do not meet the preset conditions.");
    }

    void firstChar(String s) {
        firstCharResult = s.charAt(0);
    }

    void inverse(double a) {
        if (a == 0)
            throw new IllegalArgumentException();
        invertResult = 1 / a;
    }
}
