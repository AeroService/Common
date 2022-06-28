package de.natrox.common.supplier;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CatchingSupplierTest {

    private static int valueA;
    private static long valueB;

    @Test
    void getTest1() {
        valueA = 3;
        CatchingSupplier<Integer> supplier = new CatchingSupplier<>(this::valueA);
        assertDoesNotThrow(supplier::get);
        assertEquals(3, supplier.get());
    }

    @Test
    void getTest2() {
        valueB = 4;
        CatchingSupplier<Long> supplier = new CatchingSupplier<>(this::valueB);
        assertDoesNotThrow(supplier::get);
        assertEquals(4, supplier.get());
    }

    @Test
    void getExceptionTest1() {
        valueB = 7;
        CatchingSupplier<Long> supplier = new CatchingSupplier<>(this::valueB);
        assertThrows(IllegalStateException.class, supplier::get);
    }

    int valueA() {
        return valueA;
    }

    long valueB() {
        if(valueB >= 5)
            throw new IllegalStateException();
        return valueB;
    }
}
