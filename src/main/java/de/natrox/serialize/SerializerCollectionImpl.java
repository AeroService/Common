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
import de.natrox.serialize.parse.*;
import io.leangen.geantyref.GenericTypeReflector;
import io.leangen.geantyref.TypeToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings({"unchecked"})
final class SerializerCollectionImpl implements SerializerCollection {

    final static SerializerCollection DEFAULT;

    static {
        DEFAULT = SerializerCollection
            .builder()
            .registerExact(Boolean.class, type -> Parsers.BOOLEAN)
            .registerExact(boolean.class, type -> Parsers.BOOLEAN)
            .registerExact(Character.class, type -> Parsers.CHAR)
            .registerExact(char.class, type -> Parsers.CHAR)
            .registerExact(String.class, type -> Parsers.STRING)
            .registerExact(UUID.class, type -> Parsers.UUID)
            .registerExact(Byte.class, type -> Parsers.BYTE)
            .registerExact(byte.class, type -> Parsers.BYTE)
            .registerExact(Short.class, type -> Parsers.SHORT)
            .registerExact(short.class, type -> Parsers.SHORT)
            .registerExact(Integer.class, type -> Parsers.INTEGER)
            .registerExact(int.class, type -> Parsers.INTEGER)
            .registerExact(Long.class, type -> Parsers.LONG)
            .registerExact(long.class, type -> Parsers.LONG)
            .registerExact(Float.class, type -> Parsers.FLOAT)
            .registerExact(float.class, type -> Parsers.FLOAT)
            .registerExact(Double.class, type -> Parsers.DOUBLE)
            .registerExact(double.class, type -> Parsers.DOUBLE)
            .register(Enum.class, EnumParser::create)
            .register(type -> GenericTypeReflector.isSuperType(type, Map.class), MapParser::create)
            .register(type -> true, ObjectParser::create)
            .build();
    }

    final List<RegisteredSerializer> serializers;
    private final @Nullable SerializerCollection parent;
    private final Map<Type, Function<Type, Parser<?>>> typeMatches = new ConcurrentHashMap<>();

    SerializerCollectionImpl(@Nullable SerializerCollection parent, List<RegisteredSerializer> serializers) {
        this.parent = parent;
        this.serializers = Collections.unmodifiableList(serializers);
    }

    @Override
    public <T> @Nullable Parser<T> get(@NotNull Type type) {
        Check.notNull(type, "type");
        Function<Type, Parser<?>> supplier = this.typeMatches.computeIfAbsent(type, param -> {
            for (RegisteredSerializer ent : this.serializers) {
                if (ent.matches(param)) {
                    return ent.parser();
                }
            }

            return null;
        });

        if (supplier == null) {
            if (this.parent == null) {
                return null;
            }

            return this.parent.get(type);
        }

        return (Parser<T>) supplier.apply(type);
    }

    final static class BuilderImpl implements SerializerCollection.Builder {

        private final @Nullable SerializerCollection parent;
        private final List<RegisteredSerializer> serializers = new ArrayList<>();

        BuilderImpl(@Nullable SerializerCollection parent) {
            this.parent = parent;
        }

        @Override
        public @NotNull Builder register(@NotNull Predicate<Type> test, @NotNull Function<Type, Parser<?>> supplier) {
            Check.notNull(test, "test");
            Check.notNull(supplier, "supplier");
            this.serializers.add(new RegisteredSerializer(test, supplier));
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
        private final Function<Type, Parser<?>> parser;

        RegisteredSerializer(Predicate<Type> predicate, Function<Type, Parser<?>> parser) {
            this.predicate = predicate;
            this.parser = parser;
        }

        public boolean matches(Type test) {
            return this.predicate.test(test);
        }

        public Function<Type, Parser<?>> parser() {
            return this.parser;
        }
    }
}
