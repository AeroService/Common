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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListConverterTest {

    @Test
    void testListParser() throws SerializeException {
        Converter<Object, List<String>> listConverter = ListConverter.create(new TypeToken<>() {

        });
        List<String> expected = List.of("hi", "there", "beautiful", "people");

        // round trip value
        assertEquals(expected, listConverter.read(expected));
        assertEquals(expected, listConverter.write(expected));
    }

    @Test
    void testInnerListParser() throws SerializeException {
        Converter<Object, List<String>> listConverter = ListConverter.create(new TypeToken<>() {

        });
        List<String> expected = List.of("1", "5.34235", "true", "5533333333333333333");

        // test inner type parsing
        assertEquals(expected, listConverter.read(List.<Object>of(1, 5.34235, true, 5533333333333333333L)));
        assertEquals(expected, listConverter.write(expected));
    }

    @Test
    void testListRawTypes() {
        assertTrue(
            assertThrows(IllegalArgumentException.class, () -> ListConverter.create(List.class))
                .getMessage()
                .contains("Raw types")
        );
    }
}
