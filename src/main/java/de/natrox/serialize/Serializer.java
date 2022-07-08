package de.natrox.serialize;

import io.leangen.geantyref.TypeToken;

import java.lang.reflect.Type;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface Serializer<T> {

    Object serialize(T value, Predicate<Class<?>> types);

    default Object serialize(T value, Class<?>... types) {
        return this.serialize(value, type -> Stream.of(types).anyMatch(type::isAssignableFrom));
    }

    default String serializeToString(T value) {
        return (String) this.serialize(value, type -> type.isAssignableFrom(String.class));
    }

    T deserialize(Type type, Object obj);

    default T deserialize(Class<T> type, Object obj) {
        return this.deserialize((Type) type, obj);
    }

    default T deserialize(TypeToken<T> typeToken, Object obj) {
        return this.deserialize(typeToken.getType(), obj);
    }

}
