package de.natrox.serialize;

import io.leangen.geantyref.TypeToken;

import java.lang.reflect.Type;

@FunctionalInterface
public interface Serializer<T> {

    T deserialize(Type type, Object obj);

    default T deserialize(Class<T> type, Object obj) {
        return this.deserialize((Type) type, obj);
    }

    default T deserialize(TypeToken<T> typeToken, Object obj) {
        return this.deserialize(typeToken.getType(), obj);
    }

}
