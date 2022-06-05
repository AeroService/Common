package de.natrox.common.supplier;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class SupplierTest {

    @Test
    void throwableSupplierTest() {
        ThrowableSupplier<String, IllegalAccessException> supplier = this::supplyString;
        assertThrows(IllegalArgumentException.class, supplier::get);
    }

    @Test
    void catchingSupplierTest() {
        CatchingSupplier<String> supplier = new CatchingSupplier<>(this::supplyString);
        assertDoesNotThrow(supplier::get);
    }

    public String supplyString() {
        throw new IllegalArgumentException();
    }
}
