package de.natrox.serialize;

import de.natrox.common.validate.Check;
import de.natrox.serialize.exception.SerializeException;
import io.leangen.geantyref.TypeToken;
import org.jetbrains.annotations.NotNull;

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

    public @NotNull T deserialize(@NotNull Object obj) throws SerializeException {
        Check.notNull(obj, "object");
        return this.deserialize(obj, this.typeToken);
    }
}
