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

import de.natrox.common.container.Pair;
import de.natrox.common.validate.Check;
import de.natrox.serialize.objectmapping.ObjectMapper;
import io.leangen.geantyref.TypeToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

@SuppressWarnings("unchecked")
final class SerializerCollectionImpl implements SerializerCollection {

    final static SerializerCollection DEFAULT;

    static {
        DEFAULT = SerializerCollection
            .builder()
            .register(TypeSerializers.BOOLEAN, registrable -> registrable.typeExact(Boolean.class))
            .register(TypeSerializers.PRIM_BOOLEAN, registrable -> registrable.typeExact(boolean.class))
            .register(TypeSerializers.STRING, registrable -> registrable.typeExact(String.class))
            .register(TypeSerializers.UUID, registrable -> registrable.typeExact(UUID.class))
            .register(TypeSerializers.BYTE, registrable -> registrable.typeExact(Byte.class))
            .register(TypeSerializers.PRIM_BYTE, registrable -> registrable.typeExact(byte.class))
            .register(TypeSerializers.SHORT, registrable -> registrable.typeExact(Short.class))
            .register(TypeSerializers.PRIM_SHORT, registrable -> registrable.typeExact(short.class))
            .register(TypeSerializers.INTEGER, registrable -> registrable.typeExact(Integer.class))
            .register(TypeSerializers.PRIM_INTEGER, registrable -> registrable.typeExact(int.class))
            .register(TypeSerializers.LONG, registrable -> registrable.typeExact(Long.class))
            .register(TypeSerializers.PRIM_LONG, registrable -> registrable.typeExact(long.class))
            .register(TypeSerializers.FLOAT, registrable -> registrable.typeExact(Float.class))
            .register(TypeSerializers.PRIM_FLOAT, registrable -> registrable.typeExact(float.class))
            .register(TypeSerializers.DOUBLE, registrable -> registrable.typeExact(Double.class))
            .register(TypeSerializers.PRIM_DOUBLE, registrable -> registrable.typeExact(double.class))
            .register(
                new ObjectDeserializer<>(ObjectMapper.factory()),
                registrable -> registrable.type(Object.class).inputTypeExact(new TypeToken<Map<String, Object>>(){}.getType())
            )
            .build();
    }

    final List<RegisteredSerializer> serializers;
    private final @Nullable SerializerCollection parent;
    private final Map<Pair<Type, Type>, SpecificDeserializer<?, ?>> typeMatches = new ConcurrentHashMap<>();

    SerializerCollectionImpl(@Nullable SerializerCollection parent, List<RegisteredSerializer> serializers) {
        this.parent = parent;
        this.serializers = Collections.unmodifiableList(serializers);
    }

    @Override
    public <T> @Nullable Deserializer<T> get(@NotNull Type type) {
        Check.notNull(type, "type");
        SpecificDeserializer<T, Object> deserializer = this.get(type, Object.class);

        if (deserializer == null) {
            return null;
        }

        return deserializer::deserialize;
    }

    @Override
    public @Nullable <T, U> SpecificDeserializer<T, U> get(@NotNull Type firstType, @NotNull Type secondType) {
        Check.notNull(firstType, "firstType");
        Check.notNull(secondType, "secondType");
        Pair<Type, Type> key = Pair.of(firstType, secondType);
        SpecificDeserializer<?, ?> serial = this.typeMatches.computeIfAbsent(key, param -> {
            for (RegisteredSerializer ent : this.serializers) {
                if (ent.matches(param)) {
                    return ent.serializer();
                }
            }

            return null;
        });

        if (serial == null && this.parent != null) {
            serial = this.parent.get(firstType, secondType);
        }
        return (SpecificDeserializer<T, U>) serial;
    }

    final static class BuilderImpl implements SerializerCollection.Builder {

        private final @Nullable SerializerCollection parent;
        private final List<RegisteredSerializer> serializers = new ArrayList<>();

        BuilderImpl(@Nullable SerializerCollection parent) {
            this.parent = parent;
        }

        @Override
        public <T, U> @NotNull Builder register(@NotNull SpecificDeserializer<T, U> deserializer, @NotNull Registrable<T, U> registrable) {
            if (!(registrable instanceof RegistrableImpl<T, U> registrableImpl)) {
                throw new RuntimeException();
            }

            this.serializers.add(new RegisteredSerializer(
                pair -> registrableImpl.typeTest().test(pair.first()) && registrableImpl.inputTest().test(pair.second()),
                deserializer
            ));
            return this;
        }

        @Override
        public @UnknownNullability SerializerCollection build() {
            return new SerializerCollectionImpl(this.parent, this.serializers);
        }
    }

    @SuppressWarnings("ClassCanBeRecord")
    static final class RegisteredSerializer {

        private final Predicate<Pair<Type, Type>> predicate;
        private final SpecificDeserializer<?, ?> deserializer;

        RegisteredSerializer(Predicate<Pair<Type, Type>> predicate, SpecificDeserializer<?, ?> deserializer) {
            this.predicate = predicate;
            this.deserializer = deserializer;
        }

        public boolean matches(Pair<Type, Type> test) {
            return this.predicate.test(test);
        }

        public SpecificDeserializer<?, ?> serializer() {
            return this.deserializer;
        }
    }
}
