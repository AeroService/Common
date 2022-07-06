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
import io.leangen.geantyref.TypeToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.function.Predicate;

public interface Serializer<T> {

    Serializer<String> STRING = new StringSerializer();
    Serializer<Boolean> BOOLEAN = new BooleanSerializer();
    Serializer<Character> CHAR = new CharacterSerializer();

    TypeToken<T> type();

    @NotNull T deserialize(@NotNull Object object) throws SerializeException;

    @NotNull T deserialize(@NotNull Type type, @NotNull Object object) throws SerializeException;

    Object serialize(@Nullable T object, @NotNull Predicate<Class<?>> typeSupported) throws SerializeException;

    T tryDeserialize(@Nullable Object object);

    default @NotNull Number serializeToNumber(@NotNull T value) throws SerializeException {
        throw new UnsupportedOperationException();
    }

    default @NotNull T deserializeFromNumber(@NotNull Number value) throws SerializeException {
        throw new UnsupportedOperationException();
    }

    default @NotNull CharSequence serializeToText(@NotNull T value) throws SerializeException {
        throw new UnsupportedOperationException();
    }

    default @NotNull T deserializeFromText(@NotNull CharSequence value) throws SerializeException {
        throw new UnsupportedOperationException();
    }

    default @NotNull Object reserialize(@NotNull T object) throws SerializeException {
        return this.reserialize(object, (everyClass) -> true);
    }

    default Object reserialize(@NotNull T object, @NotNull Predicate<Class<?>> typeSupported) throws SerializeException {
        return this.serialize(this.deserialize(object), typeSupported);
    }
}
