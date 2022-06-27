package de.natrox.common.function;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ThrowableFunctionTest {

    @Test
    void defaultApplyTest() {
        {
            ThrowableFunction<String, Character, RuntimeException> function = this::firstChar;
            assertEquals('f', function.apply("foo"));
            assertEquals('b', function.apply("boo"));
        }
        {
            ThrowableFunction<Double, Double, RuntimeException> function = this::inverse;
            assertEquals(1D/2, function.apply(2D));
            assertEquals(2D, function.apply(1D/2));
        }
        try {
            ThrowableFunction<Integer, Double, Exception> function = this::sqrt;
            assertEquals(4.0, function.apply(16));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void exceptionApplyTest() {
        {
            ThrowableFunction<String, Character, RuntimeException> function = this::firstChar;
            assertThrows(StringIndexOutOfBoundsException.class, ()  -> function.apply(""));
        }
        {
            ThrowableFunction<Double, Double, RuntimeException> function = this::inverse;
            assertThrows(IllegalArgumentException.class, ()  -> function.apply(0D));
        }
        {
            ThrowableFunction<Integer, Double, Exception> function = this::sqrt;
            assertThrows(Exception.class, () -> function.apply(-1));
            //sqrt(-1) would be i as the imaginary unit.
        }
    }

    char firstChar(String s) {
        return s.charAt(0);
    }

    double inverse(double a) {
        if(a == 0)
            throw new IllegalArgumentException();
        return 1 / a;
    }

    double sqrt(int a) throws Exception {
        if(a < 0)
            throw new Exception();
        return Math.sqrt(a);
    }
}
