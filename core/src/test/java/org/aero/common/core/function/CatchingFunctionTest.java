/*
 * Copyright 2020-2023 AeroService
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

package org.aero.common.core.function;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CatchingFunctionTest {

    @Test
    void testApply() {
        final CatchingFunction<Integer, Integer> function = new CatchingFunction<>(i -> i);
        assertEquals(1, function.apply(1), "Function should return the input of 1");
        assertEquals(2, function.apply(2), "Function should return the input of 2");
    }

    @Test
    void testThrowingApply() {
        final CatchingFunction<Integer, Integer> function = new CatchingFunction<>(i -> {
            this.throwException();
            return i;
        });
        assertThrows(IllegalArgumentException.class, () ->
            function.apply(-1), "Function should throw an exception if the arguments don't meet the conditions");
    }

    private void throwException() {
        throw new IllegalArgumentException();
    }
}
