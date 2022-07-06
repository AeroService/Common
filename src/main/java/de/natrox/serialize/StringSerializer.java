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

import de.natrox.serialize.exception.SerializeException;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.function.Predicate;

public final class StringSerializer extends AbstractSerializer<String> {

    public StringSerializer() {
        super(String.class);
    }

    @Override
    public @NotNull String deserialize(@NotNull Type type, @NotNull Object object) throws SerializeException {
        if (CharSequence.class.isAssignableFrom(object.getClass()))
            return this.deserializeFromText((CharSequence) object);
        if (Number.class.isAssignableFrom(object.getClass()))
            return this.deserializeFromNumber((Number) object);
        if (Character.class.isAssignableFrom(object.getClass()))
            return Serializer.CHAR.deserialize(type, object).toString();

        throw SerializeException.deserialize(type, object);
    }

    @Override
    public Object serialize(String value, @NotNull Predicate<Class<?>> typeSupported) throws SerializeException {
        if (typeSupported.test(String.class))
            return value;
        if ((typeSupported.test(Short.class) && value.length() <= 1))
            return this.serializeToNumber(value).shortValue();
        if ((typeSupported.test(Integer.class) && value.length() <= 1))
            return this.serializeToNumber(value).intValue();
        if ((typeSupported.test(Long.class) && value.length() <= 1))
            return this.serializeToNumber(value).longValue();

        throw SerializeException.serialize(value);
    }

    @Override
    public @NotNull Number serializeToNumber(@NotNull String value) {
        long number = 0;
        for (char c : value.toCharArray()) {
            number <<= 0x10;
            number += (short) c;
        }
        return number;
    }

    @Override
    public @NotNull String deserializeFromNumber(@NotNull Number value) {
        long l = value.longValue();
        StringBuilder builder = new StringBuilder();
        while (l != 0) {
            builder.append((char) (short) l);
            l >>>= 0x10;
        }
        return builder.reverse().toString();
    }

    @Override
    public @NotNull CharSequence serializeToText(@NotNull String value) {
        return value;
    }

    @Override
    public @NotNull String deserializeFromText(@NotNull CharSequence value) {
        return value.toString();
    }
}
