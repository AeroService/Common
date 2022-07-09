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

package de.natrox.serialize;

import io.leangen.geantyref.TypeToken;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SerializeTest {

    @Test
    void testBooleanSerializer() {
        assertDoesNotThrow(() -> {
            TypeToken<Boolean> typeToken = TypeToken.get(Boolean.class);

            SerializerCollection registry = SerializerCollection.defaults();
            Serializer<Boolean> serializer = registry.get(typeToken);

            assertTrue(serializer.deserialize("true", typeToken));
            assertTrue(serializer.deserialize("t", typeToken));
            assertTrue(serializer.deserialize("yes", typeToken));
            assertTrue(serializer.deserialize("1", typeToken));
            assertTrue(serializer.deserialize(1, typeToken));
            assertTrue(serializer.deserialize(true, typeToken));

            assertEquals(true, registry.deserialize("true", Boolean.class));
        });
    }
}
