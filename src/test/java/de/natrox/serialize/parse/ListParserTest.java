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
import de.natrox.serialize.parse.collection.ListParser;
import io.leangen.geantyref.TypeToken;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListParserTest {

    @Test
    void testListParser() throws SerializeException {
        Parser<List<String>> listParser = ListParser.create(new TypeToken<>() {

        });
        List<String> expected = List.of("hi", "there", "beautiful", "people");

        // round trip value
        assertEquals(expected, listParser.parse(expected));
    }

    @Test
    void testInnerListParser() throws SerializeException {
        Parser<List<String>> listParser = ListParser.create(new TypeToken<>() {

        });
        List<String> expected = List.of("1", "5.34235", "true", "5533333333333333333");

        // test inner type parsing
        assertEquals(expected, listParser.parse(List.<Object>of(1, 5.34235, true, 5533333333333333333L)));
    }

    @Test
    void testListRawTypes() {
        Parser<List<Object>> listParser = ListParser.create(List.class);

        assertTrue(
            assertThrows(SerializeException.class, () -> listParser.parse(List.of("foo", 5)))
                .getMessage()
                .contains("Raw types")
        );
    }
}
