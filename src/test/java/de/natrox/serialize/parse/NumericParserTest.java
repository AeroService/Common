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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NumericParserTest {

    @Test
    void testByteParser() throws SerializeException {
        byte expected = (byte) 65;

        // round trip value
        assertEquals(expected, Parsers.BYTE.parse(expected));

        // test negative
        assertEquals((byte) -65, Parsers.BYTE.parse(-65));

        // test too large
        assertThrows(SerializeException.class, () -> Parsers.BYTE.parse(348));

        // from float
        assertEquals(expected, Parsers.BYTE.parse(65f));

        // from string
        assertEquals(expected, Parsers.BYTE.parse("65"));

        // from hex
        assertEquals(expected, Parsers.BYTE.parse("0X41"));

        // from hex but lowercase
        assertEquals(expected, Parsers.BYTE.parse("0x41"));

        // from binary
        assertEquals(expected, Parsers.BYTE.parse(0b1000001));
    }

    @Test
    void testDouble() throws SerializeException {
        double expected = 3.1415e180d;

        // round trip value
        assertEquals(expected, Parsers.DOUBLE.parse(expected));

        // test negative
        assertEquals(-595.34e180d, Parsers.DOUBLE.parse(-595.34e180d));

        // from int
        assertEquals(448d, Parsers.DOUBLE.parse(448));

        // from string
        assertEquals(expected, Parsers.DOUBLE.parse("3.1415e180"));
    }

    @Test
    void testFloat() throws Exception {
        float expected = 3.1415f;

        // round trip value
        assertEquals(expected, Parsers.FLOAT.parse(expected));

        // test negative
        assertEquals(-595.34f, Parsers.FLOAT.parse(-595.34f));

        // test too large
        assertThrows(SerializeException.class, () -> Parsers.FLOAT.parse(13.4e129d));

        // from int
        assertEquals(448f, Parsers.FLOAT.parse(448));

        // from string
        assertEquals(expected, Parsers.FLOAT.parse("3.1415"));
    }

    @Test
    void testInt() throws Exception {
        int expected = 48888333;

        // round trip value
        assertEquals(expected, Parsers.INTEGER.parse(expected));

        // test negative
        assertEquals(-595959595, Parsers.INTEGER.parse(-595959595));

        // test too large
        assertThrows(SerializeException.class, () -> Parsers.INTEGER.parse(333339003003030L));

        // from double
        assertEquals(expected, Parsers.INTEGER.parse(48888333d));

        // with fraction
        assertThrows(SerializeException.class, () -> Parsers.INTEGER.parse(48888333.4d));

        // from string
        assertEquals(expected, Parsers.INTEGER.parse("48888333"));

        // from hex
        assertEquals(expected, Parsers.INTEGER.parse("0x2E9FA0D"));

        // from hex but lowercase
        assertEquals(expected, Parsers.INTEGER.parse("0x2e9fa0d"));

        // from binary
        assertEquals(expected, Parsers.INTEGER.parse("0b10111010011111101000001101"));
    }

    @Test
    void testLong() throws Exception {
        long expected = 48888333494404L;

        // round trip value
        assertEquals(expected, Parsers.LONG.parse(expected));

        // test negative
        assertEquals(-595959595L, Parsers.LONG.parse(-595959595));

        // from float
        assertEquals(expected, Parsers.LONG.parse(48888333494404d));

        // from string
        assertEquals(expected, Parsers.LONG.parse("48888333494404"));

        // from hex
        assertEquals(expected, Parsers.LONG.parse("0x2c76b3c06884"));

        // from binary
        assertEquals(expected, Parsers.LONG.parse("0b1011000111011010110011110000000110100010000100"));
    }

    @Test
    void testShort() throws SerializeException {
        short expected = (short) 32486;

        // round trip value
        assertEquals(expected, Parsers.SHORT.parse(expected));

        // test negative
        assertEquals((short) -32486, Parsers.SHORT.parse(-32486));

        // test too large
        assertThrows(SerializeException.class, () -> Parsers.SHORT.parse(348333333));

        // from float
        assertEquals(expected, Parsers.SHORT.parse(32486f));

        // from string
        assertEquals(expected, Parsers.SHORT.parse("32486"));

        // from hex
        assertEquals(expected, Parsers.SHORT.parse("0x7ee6"));

        // from binary
        assertEquals(expected, Parsers.SHORT.parse("0b111111011100110"));

    }
}
