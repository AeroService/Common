package de.natrox.common.runnable;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CatchingRunnableTest {

    private static int a = 1;
    private static CatchingRunnable runnable;

    @BeforeAll
    static void init() {
        runnable = new CatchingRunnable(() -> {
            if(a % 2 == 0)
                throw new IllegalStateException();
            a += a;
        });
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
