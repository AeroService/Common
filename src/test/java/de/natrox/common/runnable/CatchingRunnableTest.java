package de.natrox.common.runnable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CatchingRunnableTest {

    private static int a;

    @BeforeEach
    void init() {
        a = 1;
    }

    @Test
    void runTest1() {
        CatchingRunnable runnable = new CatchingRunnable(() -> {
            if(a % 2 == 0)
                throw new IllegalStateException();
            a += a;
        });
        assertDoesNotThrow(runnable::run);
        assertThrows(IllegalStateException.class, runnable::run);
        assertEquals(2, a);
    }
}
