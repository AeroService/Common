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
import de.natrox.serialize.parse.collection.ListParser;
import de.natrox.serialize.parse.collection.SetParser;
import de.natrox.serialize.parse.collection.array.ArrayParser;
import io.leangen.geantyref.GenericTypeReflector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.io.File;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings({"unchecked"})
final class ParserCollectionImpl implements ParserCollection {

    final static ParserCollection DEFAULT;

    static {
        DEFAULT = ParserCollection
            .builder()
            .registerExact(Boolean.class, Parsers.BOOLEAN)
            .registerExact(boolean.class, Parsers.BOOLEAN)
            .registerExact(Character.class, Parsers.CHAR)
            .registerExact(char.class, Parsers.CHAR)
            .registerExact(String.class, Parsers.STRING)
            .registerExact(URI.class, Parsers.URI)
            .registerExact(URL.class, Parsers.URL)
            .registerExact(UUID.class, Parsers.UUID)
            .registerExact(Path.class, Parsers.PATH)
            .registerExact(File.class, Parsers.FILE)
            .registerExact(Byte.class, Parsers.BYTE)
            .registerExact(byte.class, Parsers.BYTE)
            .registerExact(Short.class, Parsers.SHORT)
            .registerExact(short.class, Parsers.SHORT)
            .registerExact(Integer.class, Parsers.INTEGER)
            .registerExact(int.class, Parsers.INTEGER)
            .registerExact(Long.class, Parsers.LONG)
            .registerExact(long.class, Parsers.LONG)
            .registerExact(Float.class, Parsers.FLOAT)
            .registerExact(float.class, Parsers.FLOAT)
            .registerExact(Double.class, Parsers.DOUBLE)
            .registerExact(double.class, Parsers.DOUBLE)
            .registerProvider(Enum.class, EnumParser::create)
            .registerProvider(type -> true, ArrayParser::createObject)
            .registerExact(boolean[].class, ArrayParser.createBoolean())
            .registerExact(byte[].class, ArrayParser.createByte())
            .registerExact(char[].class, ArrayParser.createChar())
            .registerExact(double[].class, ArrayParser.createDouble())
            .registerExact(float[].class, ArrayParser.createFloat())
            .registerExact(int[].class, ArrayParser.createInteger())
            .registerExact(long[].class, ArrayParser.createLong())
            .registerExact(short[].class, ArrayParser.createShort())
            .registerProvider(type -> GenericTypeReflector.isSuperType(type, Map.class), MapParser::create)
            .registerProvider(type -> GenericTypeReflector.isSuperType(type, Set.class), SetParser::create)
            .registerProvider(type -> GenericTypeReflector.isSuperType(type, List.class), ListParser::create)
            .registerProvider(type -> true, ObjectParser::create)
            .build();
    }

    private final @Nullable ParserCollection parent;
    private final Map<Type, Function<Type, Parser<?>>> typeMatches = new ConcurrentHashMap<>();
    List<RegisteredSerializer> serializers;

    ParserCollectionImpl(@Nullable ParserCollection parent, List<RegisteredSerializer> serializers) {
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

    final static class BuilderImpl implements ParserCollection.Builder {

        private final @Nullable ParserCollection parent;
        private final List<RegisteredSerializer> serializers = new ArrayList<>();

        BuilderImpl(@Nullable ParserCollection parent) {
            this.parent = parent;
        }

        @Override
        public @NotNull Builder registerProvider(@NotNull Predicate<Type> test, @NotNull Function<Type, Parser<?>> supplier) {
            Check.notNull(test, "test");
            Check.notNull(supplier, "supplier");
            this.serializers.add(new RegisteredSerializer(test, supplier));
            return this;
        }

        @Override
        public @UnknownNullability ParserCollection build() {
            return new ParserCollectionImpl(this.parent, this.serializers);
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
