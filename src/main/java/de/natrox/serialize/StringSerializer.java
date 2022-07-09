package de.natrox.serialize;

import java.lang.reflect.Type;

final class StringSerializer extends TypeSerializer<String> {

    StringSerializer() {
        super(String.class);
    }

    @Override
    public String deserialize(Type type, Object obj) {
        return obj.toString();
    }
}
