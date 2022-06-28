package de.natrox.common.consumer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuadConsumerTest {

    private static int doMathResult;
    private static String concatResult;

    @Test
    void applyTest1() {
        QuadConsumer<Integer, Integer, Integer, Integer> consumer = this::doMath;
        consumer.accept(5, 1, 7, 2);
        assertEquals(38, doMathResult, "Consumer should give expected output 38 for 5 + 2*1 + 3*7 + 5*2.");
    }

    @Test
    void applyTest2() {
        QuadConsumer<Integer, Long, Boolean, Double> consumer = this::concat;
        consumer.accept(12, 34567L, true, 8.9D);
        assertEquals("1234567true8.9", concatResult, "Consumer should give expected output \"1234567true8.9\" as 12, 334567L, true, 8.9D get combined.");
    }

    @Test
    void nullTest() {
        QuadConsumer<Integer, Integer, Integer, Integer> consumer = this::doMath;
        assertThrows(NullPointerException.class, () ->
            consumer.accept(null, null, null, null), "Consumer should throw a NullPointerException if the arguments are null.");
    }

    void doMath(int a, int b, int c, int d) {
        doMathResult = a + 2 * b + 3 * c + 5 * d;
    }

    void concat(Object a, Object b, Object c, Object d) {
        concatResult = a.toString() + b.toString() + c.toString() + d.toString();
    }
}
