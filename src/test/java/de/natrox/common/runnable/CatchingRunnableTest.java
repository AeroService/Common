package de.natrox.common.runnable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CatchingRunnableTest {

    private static int a;

    @Test
    void defaultRunTest() {
        CatchingRunnable runnable = new CatchingRunnable(this::check);
        a = 1;
        assertDoesNotThrow(runnable::run);
        a = 2;
        assertDoesNotThrow(runnable::run);
    }

    @Test
    void exceptionRunTest() {
        CatchingRunnable runnable = new CatchingRunnable(this::check);
        a = -1;
        assertThrows(IllegalArgumentException.class,
            runnable::run, "Function should throw an exception if the arguments don't meet the conditions.");
    }

    private void check() {
        if (a <= 0)
            throw new IllegalArgumentException();
    }
}
