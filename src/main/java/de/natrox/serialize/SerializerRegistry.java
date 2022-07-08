package de.natrox.serialize;

import de.natrox.common.validate.Check;
import io.leangen.geantyref.TypeToken;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public interface SerializerRegistry {

    static @NotNull SerializerRegistry.Builder builder() {
        return null;
    }

    static @NotNull SerializerRegistry defaults() {
        return null;
    }

    <T> @NotNull Serializer<T> get(@NotNull Type type);

    default <T> @NotNull Serializer<T> get(@NotNull Class<T> type) {
        Check.notNull(type, "type");
        return this.get(type);
    }
    default <T> @NotNull Serializer<T> get(@NotNull TypeToken<T> typeToken) {
        Check.notNull(typeToken, "typeToken");
        return this.get(typeToken.getType());
    }


    interface Builder {

    }

}
