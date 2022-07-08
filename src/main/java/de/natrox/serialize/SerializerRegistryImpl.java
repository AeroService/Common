package de.natrox.serialize;

import de.natrox.common.validate.Check;
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
final class SerializerRegistryImpl implements SerializerRegistry {

    final static SerializerRegistry DEFAULT;

    static {
        DEFAULT = SerializerRegistry
            .builder()
            .registerExact(TypeSerializers.BOOLEAN)
            .build();
    }

    final List<RegisteredSerializer> serializers;
    private final @Nullable SerializerRegistry parent;
    private final Map<Type, Serializer<?>> typeMatches = new ConcurrentHashMap<>();

    SerializerRegistryImpl(@Nullable SerializerRegistry parent, List<RegisteredSerializer> serializers) {
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

            return DummySerializer.INSTANCE;
        });

        if (serial == DummySerializer.INSTANCE) {
            serial = null;
        }

        if (serial == null && this.parent != null) {
            serial = this.parent.get(type);
        }
        return (Serializer<T>) serial;
    }

    final static class BuilderImpl implements SerializerRegistry.Builder {

        private final @Nullable SerializerRegistry parent;
        private final List<RegisteredSerializer> serializers = new ArrayList<>();

        BuilderImpl(@Nullable SerializerRegistry parent) {
            this.parent = parent;
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
        public @UnknownNullability SerializerRegistry build() {
            return new SerializerRegistryImpl(this.parent, this.serializers);
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

    static final class DummySerializer implements Serializer<Void> {

        static final DummySerializer INSTANCE = new DummySerializer();

        private DummySerializer() {

        }

        @Override
        public Object serialize(Void value, Predicate<Class<?>> types) {
            throw new UnsupportedOperationException("This is a placeholder for null, should not be called directly");
        }

        @Override
        public Void deserialize(Type type, Object obj) {
            throw new UnsupportedOperationException("This is a placeholder for null, should not be called directly");
        }
    }
}
