package de.natrox.common.function;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CatchingFunctionTest {

    @Test
    void defaultApplyTest1() {
        CatchingFunction<String, Character> function = new CatchingFunction<>(this::firstChar);
        assertEquals('f', function.apply("foo"));
        assertEquals('b', function.apply("boo"));
    }

    @Test
    void defaultApplyTest2() {
        CatchingFunction<Double, Double> function = new CatchingFunction<>(this::inverse);
        assertEquals(1D / 2, function.apply(2D));
        assertEquals(2D, function.apply(1D / 2));
    }

    @Test
    void exceptionApplyTest1() {
        CatchingFunction<String, Character> function = new CatchingFunction<>(this::firstChar);
        assertThrows(StringIndexOutOfBoundsException.class, () -> function.apply(""));
    }

    @Test
    void exceptionApplyTest2() {
        CatchingFunction<Double, Double> function = new CatchingFunction<>(this::inverse);
        assertThrows(IllegalArgumentException.class, () -> function.apply(0D));
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
