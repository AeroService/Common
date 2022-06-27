package de.natrox.common.consumer;

import de.natrox.common.function.ThrowableFunction;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class ThrowableConsumerTest {

    private static char firstCharResult;
    private static double invertResult;
    private static double sqrtResult;

    @Test
    void defaultApplyTest1() {
        ThrowableConsumer<String, IllegalArgumentException> consumer = this::firstChar;
        consumer.accept("foo");
        assertEquals('f', firstCharResult);
        consumer.accept("boo");
        assertEquals('b', firstCharResult);
    }

    @Test
    void defaultApplyTest2() {
        ThrowableConsumer<Double, StringIndexOutOfBoundsException> consumer = this::inverse;
        consumer.accept(2D);
        assertEquals(1D / 2, invertResult);
        consumer.accept(1D / 2);
        assertEquals(2D, invertResult);
    }

    @Test
    void defaultApplyTest3() {
        try {
            ThrowableConsumer<Integer, Exception> consumer = this::sqrt;
            consumer.accept(16);
            assertEquals(4.0, sqrtResult);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void exceptionApplyTest1() {
        ThrowableConsumer<String, StringIndexOutOfBoundsException> consumer = this::firstChar;
        assertThrows(StringIndexOutOfBoundsException.class, () -> consumer.accept(""));
    }

    @Test
    void exceptionApplyTest2() {
        ThrowableConsumer<Double, IllegalArgumentException> consumer = this::inverse;
        assertThrows(IllegalArgumentException.class, () -> consumer.accept(0D));
    }

    @Test
    void exceptionApplyTest3() {
        ThrowableConsumer<Integer, Exception> consumer = this::sqrt;
        assertThrows(Exception.class, () -> consumer.accept(-1));
    }

    void firstChar(String s) {
        firstCharResult = s.charAt(0);
    }

    void inverse(double a) {
        if (a == 0)
            throw new IllegalArgumentException();
        invertResult = 1 / a;
    }

    void sqrt(int a) throws Exception {
        if (a < 0)
            throw new Exception();
        sqrtResult = Math.sqrt(a);
    }
}
