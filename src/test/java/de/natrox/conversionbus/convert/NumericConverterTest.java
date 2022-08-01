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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NumericConverterTest {

    @Test
    void testByteParser() throws SerializeException {
        byte expected = (byte) 65;

        // round trip value
        assertEquals(expected, Converters.BYTE.read(expected));

        // test negative
        assertEquals((byte) -65, Converters.BYTE.read(-65));

        // test too large
        assertThrows(SerializeException.class, () -> Converters.BYTE.read(348));

        // from float
        assertEquals(expected, Converters.BYTE.read(65f));

        // from string
        assertEquals(expected, Converters.BYTE.read("65"));

        // from hex
        assertEquals(expected, Converters.BYTE.read("0X41"));

        // from hex but lowercase
        assertEquals(expected, Converters.BYTE.read("0x41"));

        // from binary
        assertEquals(expected, Converters.BYTE.read(0b1000001));
    }

    @Test
    void testDouble() throws SerializeException {
        double expected = 3.1415e180d;

        // round trip value
        assertEquals(expected, Converters.DOUBLE.read(expected));

        // test negative
        assertEquals(-595.34e180d, Converters.DOUBLE.read(-595.34e180d));

        // from int
        assertEquals(448d, Converters.DOUBLE.read(448));

        // from string
        assertEquals(expected, Converters.DOUBLE.read("3.1415e180"));
    }

    @Test
    void testFloat() throws Exception {
        float expected = 3.1415f;

        // round trip value
        assertEquals(expected, Converters.FLOAT.read(expected));

        // test negative
        assertEquals(-595.34f, Converters.FLOAT.read(-595.34f));

        // test too large
        assertThrows(SerializeException.class, () -> Converters.FLOAT.read(13.4e129d));

        // from int
        assertEquals(448f, Converters.FLOAT.read(448));

        // from string
        assertEquals(expected, Converters.FLOAT.read("3.1415"));
    }

    @Test
    void testInt() throws Exception {
        int expected = 48888333;

        // round trip value
        assertEquals(expected, Converters.INTEGER.read(expected));

        // test negative
        assertEquals(-595959595, Converters.INTEGER.read(-595959595));

        // test too large
        assertThrows(SerializeException.class, () -> Converters.INTEGER.read(333339003003030L));

        // from double
        assertEquals(expected, Converters.INTEGER.read(48888333d));

        // with fraction
        assertEquals(expected, Converters.INTEGER.read(48888333.4d));

        // from string
        assertEquals(expected, Converters.INTEGER.read("48888333"));

        // from hex
        assertEquals(expected, Converters.INTEGER.read("0x2E9FA0D"));

        // from hex but lowercase
        assertEquals(expected, Converters.INTEGER.read("0x2e9fa0d"));

        // from binary
        assertEquals(expected, Converters.INTEGER.read("0b10111010011111101000001101"));
    }

    @Test
    void testLong() throws Exception {
        long expected = 48888333494404L;

        // round trip value
        assertEquals(expected, Converters.LONG.read(expected));

        // test negative
        assertEquals(-595959595L, Converters.LONG.read(-595959595));

        // from float
        assertEquals(expected, Converters.LONG.read(48888333494404d));

        // from string
        assertEquals(expected, Converters.LONG.read("48888333494404"));

        // from hex
        assertEquals(expected, Converters.LONG.read("0x2c76b3c06884"));

        // from binary
        assertEquals(expected, Converters.LONG.read("0b1011000111011010110011110000000110100010000100"));
    }

    @Test
    void testShort() throws SerializeException {
        short expected = (short) 32486;

        // round trip value
        assertEquals(expected, Converters.SHORT.read(expected));

        // test negative
        assertEquals((short) -32486, Converters.SHORT.read(-32486));

        // test too large
        assertThrows(SerializeException.class, () -> Converters.SHORT.read(348333333));

        // from float
        assertEquals(expected, Converters.SHORT.read(32486f));

        // from string
        assertEquals(expected, Converters.SHORT.read("32486"));

        // from hex
        assertEquals(expected, Converters.SHORT.read("0x7ee6"));

        // from binary
        assertEquals(expected, Converters.SHORT.read("0b111111011100110"));

    }
}
