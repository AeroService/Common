package de.natrox.serialize;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.lang.reflect.Type;

final class SerializerRegistryImpl implements SerializerRegistry {

    final static SerializerRegistry DEFAULT;

    static {
        DEFAULT = SerializerRegistry
            .builder()
            .register(TypeSerializers.BOOLEAN)
            .register(TypeSerializers.BOOLEAN_PRIMITIVE)
            .build();
    }

    @Override
    public @NotNull <T> Serializer<T> get(@NotNull Type type) {
        return null;
    }

    final static class BuilderImpl implements SerializerRegistry.Builder {

        @Override
        public @NotNull Builder register(@NotNull Type type, @NotNull Serializer<?> serializer) {
            return null;
        }

        @Override
        public @UnknownNullability SerializerRegistry build() {
            return new SerializerRegistryImpl();
        }
    }
}
