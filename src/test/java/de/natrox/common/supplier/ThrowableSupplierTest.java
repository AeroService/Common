package de.natrox.common.supplier;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ThrowableSupplierTest {

    private static int a;

    @Test
    void defaultGetTest1() {
        ThrowableSupplier<Integer, IllegalArgumentException> supplier = this::a;
        a = 1;
        assertEquals(1, supplier.get(), "Supplier should provide the input of 1.");
        a = 2;
        assertEquals(2, supplier.get(), "Supplier should provide the input of 2.");
    }

    @Test
    void defaultGetTest2() {
        ThrowableSupplier<Integer, Exception> supplier = this::exceptionA;
        try {
            a = 1;
            assertEquals(1, supplier.get(), "Supplier should provide the input of 1.");
            a = 2;
            assertEquals(2, supplier.get(), "Supplier should provide the input of 2.");
        } catch (Exception e) {
            fail("Function should not throw an exception as the arguments are valid.");
        }
    }

    @Test
    void exceptionGetTest1() {
        ThrowableSupplier<Integer, IllegalArgumentException> supplier = this::a;
        a = -1;
        assertThrows(IllegalArgumentException.class,
            supplier::get, "Supplier should throw an exception if the arguments don't meet the conditions.");
    }

    @Test
    void exceptionGetTest2() {
        ThrowableSupplier<Integer, Exception> supplier = this::exceptionA;
        a = -1;
        assertThrows(Exception.class,
            supplier::get, "Supplier should throw an exception if the arguments don't meet the conditions.");
    }

    int a() {
        if (a <= 0)
            throw new IllegalArgumentException();
        return a;
    }

    int exceptionA() throws Exception {
        if (a <= 0)
            throw new Exception();
        return a;
    }
}
