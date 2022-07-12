/*
 * Copyright 2020-2022 NatroxMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.natrox.serialize;

import de.natrox.common.validate.Check;
import de.natrox.serialize.exception.SerializeException;
import de.natrox.serialize.exception.SerializerNotFoundException;
import de.natrox.serialize.objectmapping.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.lang.reflect.Type;
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
            .registerExact(TypeSerializers.PRIM_BOOLEAN)
            .registerExact(TypeSerializers.STRING)
            .registerExact(TypeSerializers.BYTE)
            .registerExact(TypeSerializers.PRIM_BYTE)
            .registerExact(TypeSerializers.SHORT)
            .registerExact(TypeSerializers.PRIM_SHORT)
            .registerExact(TypeSerializers.INTEGER)
            .registerExact(TypeSerializers.PRIM_INTEGER)
            .registerExact(TypeSerializers.LONG)
            .registerExact(TypeSerializers.PRIM_LONG)
            .registerExact(TypeSerializers.FLOAT)
            .registerExact(TypeSerializers.PRIM_FLOAT)
            .registerExact(TypeSerializers.DOUBLE)
            .registerExact(TypeSerializers.PRIM_DOUBLE)
            .register(ObjectMapper.factory())
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
            for (RegisteredSerializer ent : this.serializers) {
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
    public @NotNull <T> T deserialize(@NotNull Object obj, Type @NotNull ... types) throws SerializeException {
        for (Type type : types) {
            try {
                Serializer<Object> serializer = this.get(type);

                if (serializer != null) {
                    return (T) serializer.deserialize(obj, type);
                }
            } catch (SerializeException ignored) {

            }
        }

        throw new SerializerNotFoundException(obj, types);
    }

    @Override
    public @NotNull <T> T deserialize(@NotNull Object obj, @NotNull Type type) throws SerializeException {
        Serializer<Object> serializer = this.get(type);

        if (serializer != null) {
            return (T) serializer.deserialize(obj, type);
        }

        throw new SerializerNotFoundException(obj, type);
    }

    final static class BuilderImpl implements SerializerCollection.Builder {

        private final @Nullable SerializerCollection parent;
        private final List<RegisteredSerializer> serializers = new ArrayList<>();

        BuilderImpl(@Nullable SerializerCollection parent) {
            this.parent = parent;
        }

        @Override
        public @NotNull Builder register(@NotNull Predicate<Type> test, @NotNull Serializer<?> serializer) {
            Check.notNull(test, "test");
            Check.notNull(serializer, "serializer");
            this.serializers.add(new RegisteredSerializer(test, serializer));
            return this;
        }

        @Override
        public @NotNull Builder register(@NotNull ChildSerializer serializer) {
            Check.notNull(serializer, "serializer");
            // FIXME: 12.07.2022 Predicate test
            this.serializers.add(new RegisteredSerializer(test -> true, serializer::deserialize));
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

        public boolean matches(Type test) {
            return this.predicate.test(test);
        }

        public Serializer<?> serializer() {
            return this.serializer;
        }
    }
}
