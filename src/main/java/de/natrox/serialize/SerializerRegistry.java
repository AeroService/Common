package de.natrox.serialize;

import de.natrox.common.builder.IBuilder;
import de.natrox.common.validate.Check;
import io.leangen.geantyref.TypeToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

public sealed interface SerializerRegistry permits SerializerRegistryImpl {

    static @NotNull SerializerRegistry.Builder builder() {
        return new SerializerRegistryImpl.BuilderImpl();
    }

    static @NotNull SerializerRegistry defaults() {
        return SerializerRegistryImpl.DEFAULT;
    }

    <T> @Nullable Serializer<T> get(@NotNull Type type);

    default <T> @Nullable Serializer<T> get(@NotNull Class<T> type) {
        Check.notNull(type, "type");
        return this.get(type);
    }

    default <T> @Nullable Serializer<T> get(@NotNull TypeToken<T> typeToken) {
        Check.notNull(typeToken, "typeToken");
        return this.get(typeToken.getType());
    }

    interface Builder extends IBuilder<SerializerRegistry> {

        SerializerRegistry.@NotNull Builder register(@NotNull Type type, @NotNull Serializer<?> serializer);

        default <T> SerializerRegistry.@NotNull Builder register(@NotNull Class<T> type, @NotNull Serializer<? super T> serializer) {
            return this.register((Type) type, serializer);
        }

        default <T> SerializerRegistry.@NotNull Builder register(@NotNull TypeToken<T> type, final Serializer<? super T> serializer) {
            return this.register(type.getType(), serializer);
        }

        default SerializerRegistry.@NotNull Builder register(@NotNull TypeSerializer<?> serializer) {
            return this.register(serializer.type().getType(), serializer);
        }

    }

}
