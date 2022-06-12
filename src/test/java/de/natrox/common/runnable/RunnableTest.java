package de.natrox.common.runnable;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class RunnableTest {

    @Test
    public void catchingTest() {
        assertThrows(IllegalArgumentException.class, () -> new CatchingRunnable(null));
        assertDoesNotThrow(() -> new CatchingRunnable(this::empty));

        {
            AtomicInteger number = new AtomicInteger();
            CatchingRunnable catchingRunnable = new CatchingRunnable(number::incrementAndGet);
            assertDoesNotThrow(catchingRunnable::run);
            assertEquals(1, number.get());
        }
        {
            CatchingRunnable catchingRunnable = new CatchingRunnable(() -> {
                throw new RuntimeException();
            });
            assertThrows(RuntimeException.class, catchingRunnable::run);
        }
    }

    public void empty() {

    }
}
