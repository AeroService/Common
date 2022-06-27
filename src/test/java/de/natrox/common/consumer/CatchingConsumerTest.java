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
        assertEquals('f', firstCharResult);
        consumer.accept("boo");
        assertEquals('b', firstCharResult);
    }

    @Test
    void defaultApplyTest2() {
        CatchingConsumer<Double> consumer = new CatchingConsumer<>(this::inverse);
        consumer.accept(2D);
        assertEquals(1D / 2, invertResult);
        consumer.accept(1D / 2);
        assertEquals(2D, invertResult);
    }

    @Test
    void exceptionApplyTest1() {
        CatchingConsumer<String> consumer = new CatchingConsumer<>(this::firstChar);
        assertThrows(StringIndexOutOfBoundsException.class, () -> consumer.accept(""));
    }

    @Test
    void exceptionApplyTest2() {
        CatchingConsumer<Double> consumer = new CatchingConsumer<>(this::inverse);
        assertThrows(IllegalArgumentException.class, () -> consumer.accept(0D));
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
