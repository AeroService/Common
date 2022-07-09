package de.natrox.serialize;

import de.natrox.common.validate.Check;
import de.natrox.serialize.exception.SerializeException;
import de.natrox.serialize.exception.SerializerNotFoundException;
import io.leangen.geantyref.GenericTypeReflector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

@SuppressWarnings("unchecked")
final class SerializerCollectionImpl implements SerializerCollection {

    final static SerializerCollection DEFAULT;

    static {
        DEFAULT = SerializerCollection
            .builder()
            .registerExact(TypeSerializers.BOOLEAN)
            .registerExact(TypeSerializers.STRING)
            .build();
    }

    final List<RegisteredSerializer> serializers;
    private final @Nullable SerializerCollection parent;
    private final Map<Type, Serializer<?>> typeMatches = new ConcurrentHashMap<>();

    SerializerCollectionImpl(@Nullable SerializerCollection parent, List<RegisteredSerializer> serializers) {
        this.parent = parent;
        this.serializers = Collections.unmodifiableList(serializers);
    }

    @Override
    public @Nullable <T> Serializer<T> get(@NotNull Type type) {
        Check.notNull(type, "type");
        Serializer<?> serial = this.typeMatches.computeIfAbsent(type, param -> {
            for (final RegisteredSerializer ent : this.serializers) {
                if (ent.matches(param)) {
                    return ent.serializer();
                }
            }

            return null;
        });

        if (serial == null && this.parent != null) {
            serial = this.parent.get(type);
        }
        return (Serializer<T>) serial;
    }

    @Override
    public @NotNull Object deserialize(@NotNull Object obj, Type @NotNull ... types) throws SerializeException {
        for (Type type : types) {
            try {
                Serializer<Object> serializer = this.get(type);

                if (serializer != null) {
                    return serializer.deserialize(obj, type);
                }
            } catch (SerializeException ignored) {

            }
        }

        throw new SerializerNotFoundException(obj, types);
    }

    @Override
    public @NotNull Object deserialize(@NotNull Object obj, @NotNull Type type) throws SerializeException {
        Serializer<Object> serializer = this.get(type);

        if (serializer != null) {
            return serializer.deserialize(obj, type);
        }

        throw new SerializerNotFoundException(obj, type);
    }

    final static class BuilderImpl implements SerializerCollection.Builder {

        private final @Nullable SerializerCollection parent;
        private final List<RegisteredSerializer> serializers = new ArrayList<>();

        BuilderImpl(@Nullable SerializerCollection parent) {
            this.parent = parent;
        }

        @NotNull
        @Override
        public <T> Builder register(@NotNull Predicate<Type> test, TypeSerializer<? super T> serializer) {
            Check.notNull(test, "test");
            Check.notNull(serializer, "serializer");
            this.serializers.add(new RegisteredSerializer(test, serializer));
            return this;
        }

        @Override
        public @NotNull Builder register(@NotNull Type type, @NotNull Serializer<?> serializer) {
            Check.notNull(type, "type");
            Check.notNull(serializer, "serializer");
            this.serializers.add(new RegisteredSerializer(test -> {
                if (GenericTypeReflector.isSuperType(type, test)) {
                    return true;
                }

                if (test instanceof WildcardType) {
                    final Type[] upperBounds = ((WildcardType) test).getUpperBounds();
                    if (upperBounds.length == 1) {
                        return GenericTypeReflector.isSuperType(type, upperBounds[0]);
                    }
                }
                return false;
            }, serializer));
            return this;
        }

        @Override
        public @NotNull Builder registerExact(@NotNull Type type, @NotNull Serializer<?> serializer) {
            Check.notNull(type, "type");
            Check.notNull(serializer, "serializer");
            this.serializers.add(new RegisteredSerializer(test -> test.equals(type), serializer));
            return this;
        }

        @Override
        public @UnknownNullability SerializerCollection build() {
            return new SerializerCollectionImpl(this.parent, this.serializers);
        }
    }

    @SuppressWarnings("ClassCanBeRecord")
    static final class RegisteredSerializer {

        private final Predicate<Type> predicate;
        private final Serializer<?> serializer;

        RegisteredSerializer(Predicate<Type> predicate, Serializer<?> serializer) {
            this.predicate = predicate;
            this.serializer = serializer;
        }

        public boolean matches(final Type test) {
            return this.predicate.test(test);
        }

        public Serializer<?> serializer() {
            return this.serializer;
        }
    }
}
