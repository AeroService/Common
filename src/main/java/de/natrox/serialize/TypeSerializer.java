package de.natrox.serialize;

import de.natrox.serialize.exception.SerializeException;
import io.leangen.geantyref.TypeToken;

public abstract class TypeSerializer<T> implements Serializer<T> {

    private final TypeToken<T> typeToken;

    protected TypeSerializer(TypeToken<T> typeToken) {
        this.typeToken = typeToken;
    }

    protected TypeSerializer(Class<T> type) {
        this.typeToken = TypeToken.get(type);
    }

    public TypeToken<T> type() {
        return this.typeToken;
    }

    public T deserialize(Object obj) throws SerializeException {
        return this.deserialize(this.typeToken, obj);
    }
}
