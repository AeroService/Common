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

package de.natrox.conversion.convert;

import de.natrox.conversion.exception.SerializeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EnumConverterTest {

    @Test
    void testEnumParser() throws SerializeException {
        Converter<Object, TestEnum> enumConverter = EnumConverter.create(TestEnum.class);

        // round trip value
        assertEquals(TestEnum.FIRST, enumConverter.read(TestEnum.FIRST));

        // lowercase input
        assertEquals(TestEnum.FIRST, enumConverter.read("first"));

        // uppercase input
        assertEquals(TestEnum.SECOND, enumConverter.read("SECOND"));

        // mixed input
        assertEquals(TestEnum.SECOND, enumConverter.read("sEcOnD"));

        // lowercase expected & lowercase input
        assertEquals(TestEnum.third, enumConverter.read("third"));

        // lowercase expected & uppercase input
        assertEquals(TestEnum.third, enumConverter.read("THIRD"));

        // case matters lowercase input
        assertEquals(TestEnum.FOURTH, enumConverter.read("FOURTH"));

        // case matters uppercase input
        assertEquals(TestEnum.fourth, enumConverter.read("fourth"));

        // invalid input
        assertThrows(SerializeException.class, () -> enumConverter.read("3rd"));
    }

    private enum TestEnum {
        FIRST,
        SECOND,
        third,
        FOURTH,
        fourth
    }
}
