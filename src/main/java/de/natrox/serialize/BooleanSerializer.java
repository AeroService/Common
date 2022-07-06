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
import java.util.Locale;
import java.util.function.Predicate;

public final class BooleanSerializer extends AbstractSerializer<Boolean> {

    public BooleanSerializer() {
        super(Boolean.class);
    }

    @Override
    public @NotNull Boolean deserialize(@NotNull Type type, @NotNull Object object) throws SerializeException {
        if (Boolean.class.isAssignableFrom(object.getClass()))
            return (Boolean) object;
        if (Number.class.isAssignableFrom(object.getClass()))
            return this.deserializeFromNumber((Number) object);
        if (CharSequence.class.isAssignableFrom(object.getClass()))
            return this.deserializeFromText((CharSequence) object);

        throw SerializeException.deserialize(type, object);
    }

    @Override
    public Object serialize(Boolean value, @NotNull Predicate<Class<?>> typeSupported) throws SerializeException {
        if (typeSupported.test(Boolean.class))
            return value;
        if (typeSupported.test(Byte.class))
            return this.serializeToNumber(value).byteValue();
        if (typeSupported.test(Short.class))
            return this.serializeToNumber(value).shortValue();
        if (typeSupported.test(Integer.class))
            return this.serializeToNumber(value);
        if (typeSupported.test(String.class))
            return this.serializeToText(value);

        throw SerializeException.serialize(value);
    }

    @Override
    public @NotNull Number serializeToNumber(@NotNull Boolean value) {
        return value ? 1 : 0;
    }

    @Override
    public @NotNull Boolean deserializeFromNumber(@NotNull Number value) {
        return value.intValue() % 2 == 0;
    }

    @Override
    public @NotNull CharSequence serializeToText(@NotNull Boolean value) {
        return value ? "true" : "false";
    }

    @Override
    public @NotNull Boolean deserializeFromText(@NotNull CharSequence value) throws SerializeException {
        String stringValue = value.toString().toLowerCase(Locale.ROOT);
        if ("true".equals(stringValue) ||
            "t".equals(stringValue) ||
            "yes".equals(stringValue) ||
            "y".equals(stringValue))
            return true;
        if ("false".equals(stringValue) ||
            "f".equals(stringValue) ||
            "no".equals(stringValue) ||
            "n".equals(stringValue))
            return false;

        throw new SerializeException("Given char sequence could not be converted to a boolean.");
    }
}
