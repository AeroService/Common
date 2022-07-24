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

public class DoubleArrayParserTest {

    @Test
    void testArraySerializerDoublePrimitive() throws SerializeException {
        Parser<double[]> parser = ArrayParser.createDouble();
        double[] expected = new double[]{1.02d, 5.66d, 3.2d, 7.9d, 9d};

        // round trip value
        assertArrayEquals(expected, parser.parse(expected));
    }
}
