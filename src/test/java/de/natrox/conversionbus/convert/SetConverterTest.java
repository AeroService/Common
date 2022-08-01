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

package de.natrox.conversionbus.convert;

import de.natrox.conversionbus.exception.SerializeException;
import io.leangen.geantyref.TypeToken;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SetConverterTest {

    @Test
    void testSetParser() throws SerializeException {
        Converter<Object, Set<String>> setConverter = SetConverter.create(new TypeToken<>() {

        });
        Set<String> expected = Set.of("hi", "there", "beautiful", "people");

        // round trip value
        assertEquals(expected, setConverter.read(expected));
        assertEquals(expected, setConverter.write(expected));
    }

    @Test
    void testInnerSetParser() throws SerializeException {
        Converter<Object, Set<String>> setConverter = SetConverter.create(new TypeToken<>() {

        });
        Set<String> expected = Set.of("1", "5.34235", "true", "5533333333333333333");

        // test inner type parsing
        assertEquals(expected, setConverter.read(Set.<Object>of(1, 5.34235, true, 5533333333333333333L)));
        assertEquals(expected, setConverter.write(expected));
    }

    @Test
    void testSetRawTypes() {
        assertTrue(
            assertThrows(IllegalArgumentException.class, () -> SetConverter.create(Set.class))
                .getMessage()
                .contains("Raw types")
        );
    }
}
