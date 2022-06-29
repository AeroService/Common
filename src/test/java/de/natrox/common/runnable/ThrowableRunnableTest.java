package de.natrox.common.runnable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ThrowableRunnableTest {

    private static int a;

    @Test
    void defaultRunTest1() {
        ThrowableRunnable<IllegalArgumentException> runnable = this::check;
        a = 1;
        assertDoesNotThrow(runnable::run);
        a = 2;
        assertDoesNotThrow(runnable::run);
    }

    @Test
    void defaultRunTest2() {
        ThrowableRunnable<Exception> runnable = this::exceptionCheck;
        a = 1;
        assertDoesNotThrow(runnable::run, "Runnable should not throw exception as the arguments are valid");
        a = 2;
        assertDoesNotThrow(runnable::run, "Runnable should not throw exception as the arguments are valid");
    }

    @Test
    void exceptionRunTest1() {
        ThrowableRunnable<Exception> runnable = this::check;
        a = -1;
        assertThrows(IllegalArgumentException.class,
            runnable::run, "Runnable should throw an exception if the arguments don't meet the conditions");
    }

    @Test
    void exceptionRunTest2() {
        ThrowableRunnable<Exception> runnable = this::check;
        a = -1;
        assertThrows(IllegalArgumentException.class,
            runnable::run, "Runnable should throw an exception if the arguments don't meet the conditions");
    }

    private void check() {
        if (a <= 0)
            throw new IllegalArgumentException();
    }

    private void exceptionCheck() throws Exception {
        if (a <= 0)
            throw new Exception();
    }
}
