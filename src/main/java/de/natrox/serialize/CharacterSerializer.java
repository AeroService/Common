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

public class CharacterSerializer extends AbstractSerializer<Character> {

    public CharacterSerializer() {
        super(Character.class);
    }

    @Override
    public @NotNull Character deserialize(@NotNull Type type, @NotNull Object object) throws SerializeException {
        if (Character.class.isAssignableFrom(object.getClass()))
            return (Character) object;
        if (Number.class.isAssignableFrom(object.getClass()))
            return this.deserializeFromNumber((Number) object);
        if (CharSequence.class.isAssignableFrom(object.getClass()))
            return this.deserializeFromText((CharSequence) object);

        throw SerializeException.deserialize(type, object);
    }

    @Override
    public Object serialize(Character value, @NotNull Predicate<Class<?>> typeSupported) throws SerializeException {
        if (typeSupported.test(Character.class))
            return value;
        if (typeSupported.test(Integer.class))
            return this.serializeToNumber(value).intValue();
        if (typeSupported.test(Short.class))
            return this.serializeToNumber(value).shortValue();
        if (typeSupported.test(String.class))
            return this.serializeToText(value);

        throw SerializeException.serialize(value);
    }

    @Override
    public @NotNull Number serializeToNumber(@NotNull Character value) {
        return (short) (int) value;
    }

    @Override
    public @NotNull Character deserializeFromNumber(@NotNull Number value) {
        return (char) value.shortValue();
    }

    @Override
    public @NotNull CharSequence serializeToText(@NotNull Character value) {
        return value.toString();
    }

    @Override
    public @NotNull Character deserializeFromText(@NotNull CharSequence value) throws SerializeException {
        if (value.length() < 1)
            throw new SerializeException("Given char sequence contains no characters.");
        return value.charAt(0);
    }
}
