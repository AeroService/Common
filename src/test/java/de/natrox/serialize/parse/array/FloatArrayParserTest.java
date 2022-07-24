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

package de.natrox.serialize.parse.array;

import de.natrox.serialize.exception.SerializeException;
import de.natrox.serialize.parse.Parser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class FloatArrayParserTest {

    @Test
    void testFloatArrayParser() throws SerializeException {
        Parser<float[]> parser = ArrayParser.createFloat();
        float[] expected = new float[]{1.02f, 5.66f, 3.2f, 7.9f, 9f};

        // round trip value
        assertArrayEquals(expected, parser.parse(expected));
    }
}
