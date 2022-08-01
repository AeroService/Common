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

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MapConverterTest {

    @Test
    void testMapParser() throws SerializeException {
        Converter<Object, Map<String, String>> mapConverter = MapConverter.create(new TypeToken<>() {

        });
        Map<String, String> expected = Map.of("url", "https://natrox.de", "success", "true");

        // round trip value
        assertEquals(expected, mapConverter.read(expected));
        assertEquals(expected, mapConverter.write(expected));
    }

    @Test
    void testInnerMapParser() throws SerializeException {
        Converter<Object, Map<String, Integer>> mapConverter = MapConverter.create(new TypeToken<>() {

        });
        Map<String, Integer> expected = Map.of("fish", 5, "bugs", 124880, "time", -1);

        // test inner type parsing
        assertEquals(expected, mapConverter.read(Map.<String, Object>of("fish", 5, "bugs", "124880", "time", "-1")));
        assertEquals(expected, mapConverter.write(expected));
    }

    @Test
    void testMapRawTypes() {
        assertTrue(
            assertThrows(IllegalArgumentException.class, () -> MapConverter.create(Map.class))
                .getMessage()
                .contains("Raw types")
        );
    }
}
