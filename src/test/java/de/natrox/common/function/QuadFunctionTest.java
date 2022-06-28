package de.natrox.common.function;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuadFunctionTest {

    @Test
    void applyTest1() {
        QuadFunction<Integer, Integer, Integer, Integer, Integer> function = this::doMath;
        assertEquals(38, function.apply(5, 1, 7, 2), "Function should return 38 for 5 + 2*1 + 3*7 + 5*2.");
    }

    @Test
    void applyTest2() {
        QuadFunction<Integer, Long, Boolean, Double, String> function = this::concat;
        assertEquals("1234567true8.9", function.apply(12, 34567L, true, 8.9D), "Function should return \"1234567true8.9\" as 12, 334567L, true, 8.9D get combined.");
    }

    @Test
    void nullTest() {
        QuadFunction<Integer, Integer, Integer, Integer, Integer> function = this::doMath;
        assertThrows(NullPointerException.class, () -> function.apply(null, null, null, null), "Function should throw a NullPointerException if the arguments are null.");
    }

    int doMath(int a, int b, int c, int d) {
        return a + 2 * b + 3 * c + 5 * d;
    }

    String concat(Object a, Object b, Object c, Object d) {
        return a.toString() + b.toString() + c.toString() + d.toString();
    }
}
