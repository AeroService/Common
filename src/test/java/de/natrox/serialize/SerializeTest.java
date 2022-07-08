package de.natrox.serialize;

import io.leangen.geantyref.TypeToken;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SerializeTest {

    @Test
    void testBooleanSerializer() {
        TypeToken<Boolean> typeToken = TypeToken.get(Boolean.class);
        Serializer<Boolean> serializer = SerializerRegistry.defaults().get(typeToken);

        assertTrue(serializer.deserialize(Boolean.class, "true"));
        assertTrue(serializer.deserialize(Boolean.class, "t"));
        assertTrue(serializer.deserialize(Boolean.class, "yes"));
        assertTrue(serializer.deserialize(Boolean.class, "1"));
        assertTrue(serializer.deserialize(Boolean.class, 1));
        assertTrue(serializer.deserialize(Boolean.class, true));
    }

}
