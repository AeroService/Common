package de.natrox.common.consumer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ThrowableBiConsumerTest {

    private static String removeResult;
    private static long productResult;
    private static int ratioResult;

    @Test
    void defaultApplyTest1() {
        ThrowableBiConsumer<String, Character, IllegalStateException> consumer = this::remove;
        consumer.accept("Hello World!", 'o');
        assertEquals("Hell Wrld!", removeResult);
        consumer.accept("bananas", 'n');
        assertEquals("baaas", removeResult);
    }

    @Test
    void defaultApplyTest2() {
        ThrowableBiConsumer<Integer, Integer, IllegalArgumentException> consumer = this::product;
        consumer.accept(2, 3);
        assertEquals(6, productResult);
        consumer.accept(8, 3);
        assertEquals(24, productResult);
    }

    @Test
    void defaultApplyTest3() {
        try {
            ThrowableBiConsumer<Integer, Integer, Exception> consumer = this::ratio;
            consumer.accept(27, 9);
            assertEquals(3, ratioResult);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void exceptionApplyTest1() {
        ThrowableBiConsumer<String, Character, IllegalStateException> consumer = this::remove;
        assertThrows(IllegalStateException.class, () -> consumer.accept("Butterfly", 'c'));
    }

    @Test
    void exceptionApplyTest2() {
        ThrowableBiConsumer<Integer, Integer, IllegalArgumentException> consumer = this::product;
        assertThrows(IllegalArgumentException.class, () -> consumer.accept(3, -5));
    }

    @Test
    void exceptionApplyTest3() {
        ThrowableBiConsumer<Integer, Integer, Exception> consumer = this::ratio;
        assertThrows(Exception.class, () -> consumer.accept(5, 2));
    }

    void remove(String s, char c) {
        if (!s.contains(String.valueOf(c)))
            throw new IllegalStateException();
        removeResult = s.replace(String.valueOf(c), "");
    }

    void product(int a, int b) {
        if (a + b < 5)
            throw new IllegalArgumentException();
        productResult = (long) a * b;
    }

    void ratio(int a, int b) throws Exception {
        if (a % b != 0)
            throw new Exception();
        ratioResult = a / b;
    }
}
