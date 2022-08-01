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

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ObjectConverterTest {

    @Test
    void testObjectParser() throws SerializeException {
        Converter<Map<String, Object>, TestObject> testObjectConverter = ObjectConverter.create(TestObject.class);

        Map<String, Object> expected = Map.of("value", 42, "name", "Bob");

        TestObject object = testObjectConverter.read(expected);
        assertEquals(42, object.value);
        assertEquals("Bob", object.name);
        assertEquals(expected, testObjectConverter.write(object));
    }

    private static class TestObject {
        private int value;
        private String name;
    }
}
