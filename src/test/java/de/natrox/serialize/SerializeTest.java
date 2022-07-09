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
