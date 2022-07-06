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

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.function.Predicate;

public class EnumSerializer extends AbstractSerializer<Enum<?>> {

    protected EnumSerializer() {
        super(Enum.class);
    }

    public @NotNull Enum<?> deserialize(@NotNull Type type, @NotNull Object object) throws SerializeException {
        String name;
        try {
            name = Serializer.STRING.deserialize(object);
        } catch (SerializeException cause) {
            throw SerializeException.deserialize(type, object, cause);
        }
        if (!(type instanceof Class<?>) || !((Class<Enum<?>>) type).isEnum())
            throw new SerializeException("Provided Type is not an Enum.");
        Class<Enum<?>> enumClass = (Class<Enum<?>>) type;
        for (Field field : enumClass.getFields()) {
            if (!field.isEnumConstant())
                continue;
            if (name.equals(field.getName())) {
                field.setAccessible(true);
                try {
                    return enumClass.cast(field.get(null));
                } catch (IllegalAccessException cause) {
                    throw SerializeException.deserialize(type, object, cause);
                }
            }
        }
        throw SerializeException.deserialize(type, object);
    }

    @Override
    public Object serialize(Enum<?> value, @NotNull Predicate<Class<?>> typeSupported) throws SerializeException {
        if (value == null)
            throw new SerializeException("Value must not be null.");
        try {
            return Serializer.STRING.serialize(value.name(), typeSupported);
        } catch (SerializeException cause) {
            throw SerializeException.serialize(value, cause);
        }
    }

    @Override
    public @NotNull Number serializeToNumber(@NotNull Enum<?> value) throws SerializeException {
        try {
            return Serializer.STRING.serializeToNumber(value.name());
        } catch (SerializeException cause) {
            throw SerializeException.serialize(value, cause);
        }
    }

    @Override
    public @NotNull CharSequence serializeToText(@NotNull Enum<?> value) throws SerializeException {
        try {
            return Serializer.STRING.serializeToText(value.name());
        } catch (SerializeException cause) {
            throw SerializeException.serialize(value, cause);
        }
    }
}
