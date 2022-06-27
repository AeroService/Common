package de.natrox.common.function;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TriFunctionTest {

    @Test
    void applyTest1() {
        TriFunction<Integer, Integer, Integer, Integer> triFunction = this::doMath;
        assertEquals(28, triFunction.apply(5, 1, 7));
    }

    @Test
    void applyTest2() {
        TriFunction<Integer, Long, Double, String> triFunction = this::concat;
        assertEquals("12345678.9", triFunction.apply(12, 34567L, 8.9D));
    }

    @Test
    void nullTest() {
        TriFunction<Integer, Integer, Integer, Integer> triFunction = this::doMath;
        assertThrows(NullPointerException.class, () -> triFunction.apply(null, null, null));
    }

    int doMath(int a, int b, int c) {
        return a + 2 * b + 3 * c;
    }

    String concat(Object a, Object b, Object c) {
        return a.toString() + b.toString() + c.toString();
    }
}
