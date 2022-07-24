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

package de.natrox.serialize.parse.array;

import de.natrox.common.validate.Check;
import de.natrox.serialize.ParserCollection;
import de.natrox.serialize.parse.Parser;
import io.leangen.geantyref.TypeToken;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public interface ArrayParser<T> extends Parser<T> {

    static <T> @NotNull ArrayParser<T[]> createObject(@NotNull Type type) {
        Check.notNull(type, "type");
        return new ObjectArrayParser<>(type, ParserCollection.defaults());
    }

    static <T> @NotNull ArrayParser<T[]> createObject(@NotNull Class<T[]> type) {
        Check.notNull(type, "type");
        return createObject((Type) type);
    }

    static <T> @NotNull ArrayParser<T[]> createObject(@NotNull TypeToken<T[]> typeToken) {
        Check.notNull(typeToken, "typeToken");
        return createObject(typeToken.getType());
    }

    static @NotNull ArrayParser<boolean[]> createBoolean(@NotNull ParserCollection collection) {
        Check.notNull(collection, "collection");
        return new BooleanArrayParser(collection);
    }

    static @NotNull ArrayParser<boolean[]> createBoolean() {
        return createBoolean(ParserCollection.defaults());
    }

    static @NotNull ArrayParser<byte[]> createByte(@NotNull ParserCollection collection) {
        Check.notNull(collection, "collection");
        return new ByteArrayParser(collection);
    }

    static @NotNull ArrayParser<byte[]> createByte() {
        return createByte(ParserCollection.defaults());
    }

    static @NotNull ArrayParser<char[]> createChar(@NotNull ParserCollection collection) {
        Check.notNull(collection, "collection");
        return new CharArrayParser(collection);
    }

    static @NotNull ArrayParser<char[]> createChar() {
        return createChar(ParserCollection.defaults());
    }

    static @NotNull ArrayParser<double[]> createDouble(@NotNull ParserCollection collection) {
        Check.notNull(collection, "collection");
        return new DoubleArrayParser(collection);
    }

    static @NotNull ArrayParser<double[]> createDouble() {
        return createDouble(ParserCollection.defaults());
    }

    static @NotNull ArrayParser<float[]> createFloat(@NotNull ParserCollection collection) {
        Check.notNull(collection, "collection");
        return new FloatArrayParser(collection);
    }

    static @NotNull ArrayParser<float[]> createFloat() {
        return createFloat(ParserCollection.defaults());
    }

    static @NotNull ArrayParser<int[]> createInteger(@NotNull ParserCollection collection) {
        Check.notNull(collection, "collection");
        return new IntegerArrayParser(collection);
    }

    static @NotNull ArrayParser<int[]> createInteger() {
        return new IntegerArrayParser(ParserCollection.defaults());
    }

    static @NotNull ArrayParser<long[]> createLong(@NotNull ParserCollection collection) {
        Check.notNull(collection, "collection");
        return new LongArrayParser(collection);
    }

    static @NotNull ArrayParser<long[]> createLong() {
        return createLong(ParserCollection.defaults());
    }

    static @NotNull ArrayParser<short[]> createShort(@NotNull ParserCollection collection) {
        Check.notNull(collection, "collection");
        return new ShortArrayParser(collection);
    }

    static @NotNull ArrayParser<short[]> createShort() {
        return createShort(ParserCollection.defaults());
    }
}
