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

package de.natrox.serialize.parse;

import java.io.File;

public final class Parsers {

    public final static Parser<Boolean> BOOLEAN = new BooleanParser();

    public final static Parser<String> STRING = new StringParser();

    public static final Parser<Character> CHAR = new CharParser();

    public final static Parser<java.util.UUID> UUID = new UuidParser();

    public final static Parser<File> FILE = new FileParser();

    public final static Parser<Byte> BYTE = new ByteParser();

    public final static Parser<Short> SHORT = new ShortParser();

    public final static Parser<Integer> INTEGER = new IntegerParser();

    public final static Parser<Long> LONG = new LongParser();

    public final static Parser<Float> FLOAT = new FloatParser();

    public final static Parser<Double> DOUBLE = new DoubleParser();

    private Parsers() {
        throw new UnsupportedOperationException();
    }

}
