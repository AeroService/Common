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

package org.aero.common.core.util;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UuidUtilTest {

    @SuppressWarnings("ConstantConditions")
    @Test
    void testFromName() {
        assertEquals(UuidUtil.fromName("foo"), UuidUtil.fromName("foo"));
        assertNotEquals(UuidUtil.fromName("foo"), UuidUtil.fromName("boo"));
        assertThrows(IllegalArgumentException.class, () -> UuidUtil.fromName(null));
        Assertions.assertDoesNotThrow(() -> UuidUtil.fromName(""));
    }
}
