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

package de.natrox.serialize.parse.collection.array;

import de.natrox.serialize.ParserCollection;
import de.natrox.serialize.parse.Parser;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public interface ArrayParser<T> extends Parser<T> {

    static <T> @NotNull ArrayParser<T[]> createObject(Type type) {
        return new ObjectArrayParser<>(type, ParserCollection.defaults());
    }

    static @NotNull ArrayParser<boolean[]> createBoolean() {
        return new BooleanArrayParser(ParserCollection.defaults());
    }

    static @NotNull ArrayParser<byte[]> createByte() {
        return new ByteArrayParser(ParserCollection.defaults());
    }

    static @NotNull ArrayParser<char[]> createChar() {
        return new CharArrayParser(ParserCollection.defaults());
    }

    static @NotNull ArrayParser<double[]> createDouble() {
        return new DoubleArrayParser(ParserCollection.defaults());
    }

    static @NotNull ArrayParser<float[]> createFloat() {
        return new FloatArrayParser(ParserCollection.defaults());
    }

    static @NotNull ArrayParser<int[]> createInteger() {
        return new IntegerArrayParser(ParserCollection.defaults());
    }

    static @NotNull ArrayParser<long[]> createLong() {
        return new LongArrayParser(ParserCollection.defaults());
    }

    static @NotNull ArrayParser<short[]> createShort() {
        return new ShortArrayParser(ParserCollection.defaults());
    }
}
