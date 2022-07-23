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

package de.natrox.serialize.parse;

import de.natrox.serialize.exception.SerializeException;
import io.leangen.geantyref.TypeToken;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MapParserTest {

    @Test
    void testMapParser() throws SerializeException {
        Parser<Map<String, String>> mapParser = MapParser.create(new TypeToken<>() {

        });
        Map<String, String> expected = Map.of("url", "https://natrox.de", "success", "true");

        // round trip value
        assertEquals(expected, mapParser.parse(expected));
    }

    @Test
    void testInnerMapParser() throws SerializeException {
        Parser<Map<String, Integer>> mapParser = MapParser.create(new TypeToken<>() {

        });
        Map<String, Integer> expected = Map.of("fish", 5, "bugs", 124880, "time", -1);

        // test inner type parsing
        assertEquals(expected, mapParser.parse(Map.<String, Object>of("fish", 5, "bugs", "124880", "time", "-1")));
    }

    @Test
    void testMapRawTypes() {
        Parser<Map<Object, Object>> mapParser = MapParser.create(Map.class);

        assertTrue(
            assertThrows(SerializeException.class, () -> mapParser.parse(Map.of("foo", 5)))
                .getMessage()
                .contains("Raw types")
        );
    }
}
