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

import java.util.UUID;

public final class TypeSerializers {

    public final static TypeDeserializer<Boolean> BOOLEAN = new BooleanDeserializer(Boolean.class);

    public final static TypeDeserializer<Boolean> PRIM_BOOLEAN = new BooleanDeserializer(boolean.class);

    public final static TypeDeserializer<String> STRING = new StringDeserializer();

    public static final TypeDeserializer<Character> CHAR = new CharDeserializer(Character.class);

    public static final TypeDeserializer<Character> PRIM_CHAR = new CharDeserializer(char.class);

    public final static TypeDeserializer<UUID> UUID = new UuidDeserializer();

    public final static TypeDeserializer<Byte> BYTE = new ByteDeserializer(Byte.class);

    public final static TypeDeserializer<Byte> PRIM_BYTE = new ByteDeserializer(byte.class);

    public final static TypeDeserializer<Short> SHORT = new ShortDeserializer(Short.class);

    public final static TypeDeserializer<Short> PRIM_SHORT = new ShortDeserializer(short.class);

    public final static TypeDeserializer<Integer> INTEGER = new IntegerDeserializer(Integer.class);

    public final static TypeDeserializer<Integer> PRIM_INTEGER = new IntegerDeserializer(int.class);

    public final static TypeDeserializer<Long> LONG = new LongDeserializer(Long.class);

    public final static TypeDeserializer<Long> PRIM_LONG = new LongDeserializer(long.class);

    public final static TypeDeserializer<Float> FLOAT = new FloatDeserializer(Float.class);

    public final static TypeDeserializer<Float> PRIM_FLOAT = new FloatDeserializer(float.class);

    public final static TypeDeserializer<Double> DOUBLE = new DoubleDeserializer(Double.class);

    public final static TypeDeserializer<Double> PRIM_DOUBLE = new DoubleDeserializer(double.class);

    private TypeSerializers() {
        throw new UnsupportedOperationException();
    }
}
