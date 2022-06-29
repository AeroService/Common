package de.natrox.common.supplier;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CatchingSupplierTest {

    private static int a;

    @Test
    void defaultGetTest() {
        CatchingSupplier<Integer> supplier = new CatchingSupplier<>(this::a);
        a = 1;
        assertEquals(1, supplier.get(), "Supplier should provide the input of 1");
        a = 2;
        assertEquals(2, supplier.get(), "Supplier should provide the input of 2");
    }

    @Test
    void exceptionGetTest() {
        CatchingSupplier<Integer> supplier = new CatchingSupplier<>(this::a);
        a = -1;
        assertThrows(IllegalArgumentException.class,
            supplier::get, "Supplier should throw an exception if the arguments don't meet the conditions");
    }

    int a() {
        if (a <= 0)
            throw new IllegalArgumentException();
        return a;
    }
}
