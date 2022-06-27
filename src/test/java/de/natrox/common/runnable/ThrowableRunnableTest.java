package de.natrox.common.runnable;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ThrowableRunnableTest {

    private static int a;
    private static ThrowableRunnable<Exception> runnable;

    @BeforeAll
    static void init() {
        runnable = () -> {
            if(a % 2 == 0)
                throw new Exception();
            a += a;
        };
    }

    @Test
    void runTest1() {
        assertDoesNotThrow(runnable::run);
        assertEquals(2, a);
    }

    @Test
    void runTest2() {
        assertThrows(IllegalStateException.class, runnable::run);
        assertEquals(2, a);
    }
}
