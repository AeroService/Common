package de.natrox.common.function;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ThrowableBiFunctionTest {

    @Test
    void defaultApplyTest() {
        {
            ThrowableBiFunction<String, Character, String , IllegalStateException> function = this::remove;
            assertEquals("Hell Wrld!", function.apply("Hello World!", 'o'));
            assertEquals("baaas", function.apply("bananas", 'n'));
        }
        {
            ThrowableBiFunction<Integer, Integer, Long, IllegalArgumentException> function = this::product;
            assertEquals(6, function.apply(2, 3));
            assertEquals(24, function.apply(8,3));
        }
        try {
            ThrowableBiFunction<Integer, Integer, Integer, Exception> function = this::ratio;
            assertEquals(3, function.apply(27, 9));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void exceptionApplyTest() {
        {
            ThrowableBiFunction<String, Character, String , IllegalStateException> function = this::remove;
            assertThrows(IllegalStateException.class, ()  -> function.apply("Butterfly", 'c'));
        }
        {
            ThrowableBiFunction<Integer, Integer, Long, IllegalArgumentException> function = this::product;
            assertThrows(IllegalArgumentException.class, ()  -> function.apply(3, -5));
        }
        {
            ThrowableBiFunction<Integer, Integer, Integer, Exception> function = this::ratio;
            assertThrows(Exception.class, () -> function.apply(5, 2));
        }
    }

    String remove(String s, char c) {
        if(!s.contains(String.valueOf(c)))
            throw new IllegalStateException();
        return s.replace(String.valueOf(c), "");
    }

    long product(int a, int b) {
        if(a + b < 5)
            throw new IllegalArgumentException();
        return (long) a * b;
    }

    int ratio(int a, int b) throws Exception {
        if(a % b != 0)
            throw new Exception();
        return a / b;
    }
}
