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

public final class TypeSerializers {

    public final static TypeSerializer<Boolean> BOOLEAN = new BooleanSerializer(Boolean.class);

    public final static TypeSerializer<Boolean> PRIM_BOOLEAN = new BooleanSerializer(boolean.class);

    public final static TypeSerializer<String> STRING = new StringSerializer();

    public final static TypeSerializer<Byte> BYTE = new ByteSerializer(Byte.class);

    public final static TypeSerializer<Byte> PRIM_BYTE = new ByteSerializer(byte.class);

    public final static TypeSerializer<Short> SHORT = new ShortSerializer(Short.class);

    public final static TypeSerializer<Short> PRIM_SHORT = new ShortSerializer(short.class);

    public final static TypeSerializer<Integer> INTEGER = new IntegerSerializer(Integer.class);

    public final static TypeSerializer<Integer> PRIM_INTEGER = new IntegerSerializer(int.class);

    public final static TypeSerializer<Long> LONG = new LongSerializer(Long.class);

    public final static TypeSerializer<Long> PRIM_LONG = new LongSerializer(long.class);

    public final static TypeSerializer<Float> FLOAT = new FloatSerializer(Float.class);

    public final static TypeSerializer<Float> PRIM_FLOAT = new FloatSerializer(float.class);

    public final static TypeSerializer<Double> DOUBLE = new DoubleSerializer(Double.class);

    public final static TypeSerializer<Double> PRIM_DOUBLE = new DoubleSerializer(double.class);

    private TypeSerializers() {
        throw new UnsupportedOperationException();
    }
}
