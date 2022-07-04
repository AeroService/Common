/*
 * Copyright 2020-2022 NatroxMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
