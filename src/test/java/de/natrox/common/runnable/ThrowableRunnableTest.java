package de.natrox.common.runnable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ThrowableRunnableTest {

    private static int a;

    @BeforeEach
    void init() {
        a = 1;
    }

    @Test
    void runTest1() {
        ThrowableRunnable<IllegalStateException> runnable = () -> {
            if(a % 2 == 0)
                throw new IllegalStateException();
            a += a;
        };
        assertDoesNotThrow(runnable::run);
        assertThrows(IllegalStateException.class, runnable::run);
        assertEquals(2, a);
    }

    @Test
    void runTest2() {
        ThrowableRunnable<Exception> runnable = () -> {
            if(a % 2 == 0)
                throw new Exception();
            a += a;
        };
        assertDoesNotThrow(runnable::run);
        assertThrows(Exception.class, runnable::run);
        assertEquals(2, a);
    }
}
