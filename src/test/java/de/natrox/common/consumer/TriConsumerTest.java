package de.natrox.common.consumer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TriConsumerTest {

    private static int doMathResult;
    private static String concatResult;

    @Test
    void acceptTest1() {
        TriConsumer<Integer, Integer, Integer> consumer = this::doMath;
        consumer.accept(5, 1, 7);
        assertEquals(28, doMathResult, "Consumer should give expected output 38 for 5 + 2*1 + 3*7.");
    }

    @Test
    void applyTest2() {
        TriConsumer<Integer, Long, Double> consumer = this::concat;
        consumer.accept(12, 34567L, 8.9D);
        assertEquals("12345678.9", concatResult, "Consumer should give expected output \"12345678.9\" as 12, 34567L, 8.9D get combined.");
    }

    @Test
    void nullTest() {
        TriConsumer<Integer, Integer, Integer> consumer = this::doMath;
        assertThrows(NullPointerException.class, () ->
            consumer.accept(null, null, null), "Consumer should throw a NullPointerException if the arguments are null.");
    }

    void doMath(int a, int b, int c) {
        doMathResult = a + 2 * b + 3 * c;
    }

    void concat(Object a, Object b, Object c) {
        concatResult = a.toString() + b.toString() + c.toString();
    }
}
