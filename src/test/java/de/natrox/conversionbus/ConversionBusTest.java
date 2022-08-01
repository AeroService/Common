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

package de.natrox.conversionbus;

import de.natrox.conversionbus.convert.Converter;
import de.natrox.conversionbus.convert.Converters;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConversionBusTest {

    @Test
    void testGet() {
        Converter<Object, Boolean> boolConverter = ConversionBus.defaults().get(boolean.class);
        assertEquals(Converters.BOOLEAN, boolConverter);

        ConversionBus conversionBus = ConversionBus.builder().build();
        assertTrue(assertThrows(IllegalArgumentException.class, () -> conversionBus.get(boolean.class))
            .getMessage()
            .contains("No type enforcer available for type"));
    }
}
