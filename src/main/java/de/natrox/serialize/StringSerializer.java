package de.natrox.serialize;

import java.lang.reflect.Type;
import java.util.function.Predicate;

final class StringSerializer extends TypeSerializer<String> {

    StringSerializer() {
        super(String.class);
    }

    @Override
    public Object serialize(String value, Predicate<Class<?>> types) {
        return value;
    }

    @Override
    public String deserialize(Type type, Object obj) {
        return obj.toString();
    }
}
