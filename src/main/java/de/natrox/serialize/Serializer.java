package de.natrox.serialize;

import de.natrox.common.validate.Check;
import de.natrox.serialize.exception.SerializeException;
import io.leangen.geantyref.TypeToken;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

@FunctionalInterface
public interface Serializer<T> {

    @NotNull T deserialize(@NotNull Object obj, @NotNull Type type) throws SerializeException;

    default T deserialize(@NotNull Object obj, @NotNull Class<T> type) throws SerializeException {
        Check.notNull(obj, "object");
        Check.notNull(type, "type");
        return this.deserialize(obj, (Type) type);
    }

    default T deserialize(@NotNull Object obj, @NotNull TypeToken<T> typeToken) throws SerializeException {
        Check.notNull(obj, "object");
        Check.notNull(typeToken, "typeToken");
        return this.deserialize(obj, typeToken.getType());
    }
}
