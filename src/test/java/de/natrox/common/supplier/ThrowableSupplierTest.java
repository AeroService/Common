package de.natrox.common.supplier;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ThrowableSupplierTest {

    private static int valueA;
    private static long valueB;
    private static String valueC;

    @Test
    void getTest1() {
        valueA = 3;
        ThrowableSupplier<Integer, RuntimeException> supplier = this::valueA;
        assertDoesNotThrow(supplier::get);
        assertEquals(3, supplier.get());
    }

    @Test
    void getTest2() {
        valueB = 4;
        ThrowableSupplier<Long, IllegalStateException> supplier = this::valueB;
        assertDoesNotThrow(supplier::get);
        assertEquals(4, supplier.get());
    }

    @Test
    void getTest3() {
        valueC = "foo";
        ThrowableSupplier<String, Exception> supplier = this::valueC;
        assertDoesNotThrow(supplier::get);
        try {
            assertEquals("foo", supplier.get());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void getExceptionTest1() {
        valueB = 7;
        ThrowableSupplier<Long, IllegalStateException> supplier = this::valueB;
        assertThrows(IllegalStateException.class, supplier::get);
    }

    @Test
    void getExceptionTest2() {
        valueC = "boo";
        ThrowableSupplier<String, Exception> supplier = this::valueC;
        assertThrows(Exception.class, supplier::get);
    }

    int valueA() {
        return valueA;
    }

    long valueB() {
        if(valueB >= 5)
            throw new IllegalStateException();
        return valueB;
    }

    String valueC() throws Exception {
        if(!Objects.equals(valueC, "foo"))
            throw new Exception();
        return valueC;
    }
}
