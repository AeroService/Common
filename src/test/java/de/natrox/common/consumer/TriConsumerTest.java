package de.natrox.common.consumer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TriConsumerTest {

    private static AtomicInteger mathResult;
    private static String concatResult;

    @BeforeAll
    static void init() {
        mathResult = new AtomicInteger();
    }

    @Test
    void acceptTest1() {
        TriConsumer<Integer, Integer, Integer> consumer = this::doMath;
        consumer.accept(5, 1, 7);
        assertEquals(28, mathResult.get());
    }

    @Test
    void applyTest2() {
        TriConsumer<Integer, Long, Double> consumer = this::concat;
        consumer.accept(12, 34567L, 8.9D);
        assertEquals("12345678.9", concatResult);
    }

    @Test
    void nullTest() {
        TriConsumer<Integer, Integer, Integer> consumer = this::doMath;
        assertThrows(NullPointerException.class, () -> consumer.accept(null, null, null));
    }

    void doMath(int a, int b, int c) {
        mathResult.set(a + 2 * b + 3 * c);
    }

    void concat(Object a, Object b, Object c) {
        concatResult = a.toString() + b.toString() + c.toString();
    }
}
