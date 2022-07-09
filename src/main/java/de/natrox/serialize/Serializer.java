package de.natrox.serialize;

import de.natrox.serialize.exception.SerializeException;
import io.leangen.geantyref.TypeToken;

import java.lang.reflect.Type;

@FunctionalInterface
public interface Serializer<T> {

    T deserialize(Type type, Object obj) throws SerializeException;

    default T deserialize(Class<T> type, Object obj) throws SerializeException {
        return this.deserialize((Type) type, obj);
    }

    default T deserialize(TypeToken<T> typeToken, Object obj) throws SerializeException {
        return this.deserialize(typeToken.getType(), obj);
    }
}
