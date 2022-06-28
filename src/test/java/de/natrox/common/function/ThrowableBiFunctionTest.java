package de.natrox.common.function;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ThrowableBiFunctionTest {

    @Test
    void defaultApplyTest1() {
        ThrowableBiFunction<String, Character, String, IllegalStateException> function = this::remove;
        assertEquals("Hell Wrld!", function.apply("Hello World!", 'o'), "Function should return \"Hell Wrld!\" as \"Hello World!\" without 'o's.");
        assertEquals("baaas", function.apply("bananas", 'n'), "Function should return \"baaas!\" as \"bananas!\" without 'n's.");
    }

    @Test
    void defaultApplyTest2() {
        ThrowableBiFunction<Integer, Integer, Long, IllegalArgumentException> function = this::product;
        assertEquals(6, function.apply(2, 3), "Function should return 6 as 2 * 3.");
        assertEquals(24, function.apply(8, 3), "Function should return 24 as 8 * 3.");
    }

    @Test
    void defaultApplyTest3() {
        try {
            ThrowableBiFunction<Integer, Integer, Integer, Exception> function = this::ratio;
            assertEquals(3, function.apply(27, 9), "Function should return 3 as 27/9.");
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void exceptionApplyTest1() {
        ThrowableBiFunction<String, Character, String, IllegalStateException> function = this::remove;
        assertThrows(IllegalStateException.class, () -> function.apply("Butterfly", 'c'), "Function should throw IllegalStateException if \"Butterfly\" does not contain any 'c's.");
    }

    @Test
    void exceptionApplyTest2() {
        ThrowableBiFunction<Integer, Integer, Long, IllegalArgumentException> function = this::product;
        assertThrows(IllegalArgumentException.class, () -> function.apply(3, -5), "Function should throw an exception if the arguments do not meet the preset conditions.");
    }

    @Test
    void exceptionApplyTest3() {
        ThrowableBiFunction<Integer, Integer, Integer, Exception> function = this::ratio;
        assertThrows(Exception.class, () -> function.apply(5, 2), "Function should throw an exception if the arguments do not meet the preset conditions.");
    }

    String remove(String s, char c) {
        if (!s.contains(String.valueOf(c)))
            throw new IllegalStateException();
        return s.replace(String.valueOf(c), "");
    }

    long product(int a, int b) {
        if (a + b < 5)
            throw new IllegalArgumentException();
        return (long) a * b;
    }

    int ratio(int a, int b) throws Exception {
        if (a % b != 0)
            throw new Exception();
        return a / b;
    }
}
