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
import io.leangen.geantyref.GenericTypeReflector;
import io.leangen.geantyref.TypeToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.function.Predicate;

public abstract class AbstractSerializer<T> implements Serializer<T> {

    private final TypeToken<T> type;

    protected AbstractSerializer(final TypeToken<T> type) {
        final Type boxed = GenericTypeReflector.box(type.getType());
        this.type = boxed == type.getType() ? type : (TypeToken<T>) TypeToken.get(boxed);
    }

    protected AbstractSerializer(final Class<T> type) {
        if (type.getTypeParameters().length > 0)
            throw new IllegalArgumentException("Provide as a TypeToken!");
        this.type = TypeToken.get(type);
    }

    AbstractSerializer(final Type type) {
        this.type = (TypeToken<T>) TypeToken.get(type);
    }

    @Override
    public final TypeToken<T> type() {
        return this.type;
    }

    @Override
    public final @NotNull T deserialize(final @NotNull Object object) throws SerializeException {
        @Nullable T possible = this.cast(object);
        if (!Objects.isNull(possible))
            return possible;

        return this.deserialize(this.type.getType(), object);
    }

    @Override
    public abstract @NotNull T deserialize(@NotNull Type type, final @NotNull Object object) throws SerializeException;

    @Override
    public abstract Object serialize(T value, @NotNull Predicate<Class<?>> typeSupported) throws SerializeException;

    @Override
    public final @Nullable T tryDeserialize(final @Nullable Object object) {
        if (Objects.isNull(object))
            return null;

        try {
            return deserialize(object);
        } catch (SerializeException e) {
            return null;
        }
    }

    private @Nullable T cast(final Object object) {
        final Class<?> rawType = GenericTypeReflector.erase(this.type.getType());
        if (rawType.isInstance(object))
            return (T) object;
        return null;
    }
}
