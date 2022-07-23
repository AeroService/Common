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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EnumParserTest {

    @Test
    void testEnumParser() throws SerializeException {
        Parser<TestEnum> enumParser = EnumParser.create(TestEnum.class);

        // round trip value
        assertEquals(TestEnum.FIRST, enumParser.parse(TestEnum.FIRST));

        // lowercase input
        assertEquals(TestEnum.FIRST, enumParser.parse("first"));

        // uppercase input
        assertEquals(TestEnum.SECOND, enumParser.parse("SECOND"));

        // mixed input
        assertEquals(TestEnum.SECOND, enumParser.parse("sEcOnD"));

        // lowercase expected & lowercase input
        assertEquals(TestEnum.third, enumParser.parse("third"));

        // lowercase expected & uppercase input
        assertEquals(TestEnum.third, enumParser.parse("THIRD"));

        // case matters lowercase input
        assertEquals(TestEnum.FOURTH, enumParser.parse("FOURTH"));

        // case matters uppercase input
        assertEquals(TestEnum.fourth, enumParser.parse("fourth"));

        // invalid input
        assertThrows(SerializeException.class, () -> enumParser.parse("3rd"));
    }

    private enum TestEnum {
        FIRST,
        SECOND,
        third,
        FOURTH,
        fourth
    }
}
