package de.natrox.common.supplier;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class SupplierTest {

    @Test
    void throwableSupplierTest() {
        ThrowableSupplier<String, IllegalAccessException> supplier = this::supplyString;
        assertThrows(IllegalArgumentException.class, supplier::get);
    }

    @Test
    void catchingSupplierTest() {
        CatchingSupplier<String> supplier = new CatchingSupplier<>(this::supplyString);
        assertThrows(IllegalArgumentException.class, supplier::get);
    }

    public String supplyString() throws IllegalArgumentException {
        throw new IllegalArgumentException();
    }
}
