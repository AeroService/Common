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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SetParserTest {

    @Test
    void testSetParser() throws SerializeException {
        Parser<Set<String>> setParser = SetParser.create(new TypeToken<>() {

        });
        Set<String> expected = Set.of("hi", "there", "beautiful", "people");

        // round trip value
        assertEquals(expected, setParser.parse(expected));
    }

    @Test
    void testInnerSetParser() throws SerializeException {
        Parser<Set<String>> setParser = SetParser.create(new TypeToken<>() {

        });
        Set<String> expected = Set.of("1", "5.34235", "true", "5533333333333333333");

        // test inner type parsing
        assertEquals(expected, setParser.parse(Set.<Object>of(1, 5.34235, true, 5533333333333333333L)));
    }

    @Test
    void testSetRawTypes() {
        Parser<Set<Object>> setParser = SetParser.create(Set.class);

        assertTrue(
            assertThrows(SerializeException.class, () -> setParser.parse(List.of("foo", 5)))
                .getMessage()
                .contains("Raw types")
        );
    }
}
