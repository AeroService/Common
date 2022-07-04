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

package de.natrox.common.function;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

final class SingleFunctionTest {

    @Test
    void defaultApplyTest() {
        SingleTypeFunction<Integer> function = integer -> integer;
        assertEquals(6, function.apply(6), "Function should return the input 6");
        assertEquals(9, function.apply(6), "Function should return the input 6");
    }

    @Test
    void nullApplyTest() {
        SingleTypeFunction<Integer> function = integer -> integer;
        assertNull(function.apply(null), "Function should return the input null");
    }
}
