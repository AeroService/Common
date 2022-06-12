package de.natrox.common.function;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FunctionTest {

    @Test
    public void triFunctionTest() {
        TriFunction<Byte, Short, Float, String> function = this::concat;
        assertEquals("121234.5", function.apply((byte) 12, (short) 123, 4.5F));
        assertEquals('4', function.andThen(this::firstChar).apply((byte) 4, (short) 1, 0.2F));
        assertThrows(RuntimeException.class, () -> function.andThen(this::runtimeException).apply((byte) 4, (short) 1, 0.2F));
    }

    @Test
    public void quadFunctionTest() {
        QuadFunction<Long, Integer, Float, Character, String> function = this::concat;
        assertEquals("123456789101234.5F", function.apply(12345678910L, 123, 4.5F, 'F'));
        assertEquals('4', function.andThen(this::firstChar).apply(4L, 1, 0.2F, 'C'));
        assertThrows(RuntimeException.class, () -> function.andThen(this::runtimeException).apply(4L, 1, 0.2F, 'C'));
    }

    @Test
    public void throwableFunctionTest() {
        try {
            ThrowableFunction<String, Character, Exception> function = this::firstChar;
            assertEquals('f', function.apply("foo"));
            assertDoesNotThrow(() -> function.apply("foo"));
        } catch (Exception e) {
            fail();
        }

        try {
            ThrowableFunction<String, Character, Exception> function = this::exception;
            assertThrows(Exception.class, () -> function.apply("foo"));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void throwableBiFunctionTest() {
        try {
            ThrowableBiFunction<String, String, Character, Exception> function = this::firstChar;
            assertEquals('f', function.apply("foo", "fuu"));
            assertDoesNotThrow(() -> function.apply("foo", "fuu"));
        } catch (Exception e) {
            fail();
        }

        try {
            ThrowableBiFunction<String, String, Character, Exception> function = this::exception;
            assertThrows(Exception.class, () -> function.apply("foo", "fuu"));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void catchingFunctionTest() {
        {
            CatchingFunction<String, Character> function = new CatchingFunction<>(this::firstChar);
            assertEquals('f', function.apply("foo"));
            assertDoesNotThrow(() -> function.apply("foo"));
        }
        try {
            CatchingFunction<String, Character> function = new CatchingFunction<>(this::runtimeException);
            assertThrows(Exception.class, () -> function.apply("foo"));
        } catch (Exception e) {
            fail();
        }
    }

    public char runtimeException(String... a) {
        throw new RuntimeException();
    }

    public char exception(String... a) throws Exception {
        throw new Exception();
    }

    public char firstChar(String a, Object... objects) {
        return a.charAt(0);
    }

    public String concat(Object... objects) {
        StringBuilder builder = new StringBuilder();
        for (Object object : objects)
            builder.append(object.toString());
        return builder.toString();
    }
}
