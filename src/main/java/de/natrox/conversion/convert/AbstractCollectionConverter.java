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

package de.natrox.conversion.convert;

import de.natrox.common.validate.Check;
import de.natrox.conversion.ConversionBus;
import de.natrox.conversion.exception.SerializeException;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.function.BiFunction;

@SuppressWarnings("unchecked")
abstract class AbstractCollectionConverter<T, U extends Collection<T>> implements Converter<Object, U> {

    private final Type entryType;
    private final Converter<Object, T> entryConverter;

    AbstractCollectionConverter(Type entryType, Converter<Object, T> entryConverter) {
        this.entryType = entryType;
        this.entryConverter = entryConverter;
    }

    static <T, U extends Collection<T>, R extends AbstractCollectionConverter<T, U>> R create(Type type, ConversionBus collection, BiFunction<Type, Converter<Object, T>, R> function) {
        Check.notNull(type, "type");
        Check.notNull(collection, "collection");
        Check.argCondition(!(type instanceof ParameterizedType), "Raw types are not supported for collections");

        Type entryType = ((ParameterizedType) type).getActualTypeArguments()[0];
        Converter<Object, T> converter = collection.get(entryType);

        return function.apply(entryType, converter);
    }

    @Override
    public @NotNull U read(@NotNull Object value) throws SerializeException {
        if (value instanceof Collection<?> values) {
            final U ret = (U) this.createNew(values.size(), this.entryType);
            for (Object entry : values) {
                ret.add(this.entryConverter.read(entry));
            }
            return ret;
        } else {
            final U ret = (U) this.createNew(1, this.entryType);
            ret.add(this.entryConverter.read(value));
            return ret;
        }
    }

    @Override
    public @NotNull Object write(@NotNull U value) throws SerializeException {
        final Collection<Object> ret = this.createNew(value.size(), Object.class);
        for (T entry : value) {
            ret.add(this.entryConverter.write(entry));
        }
        return ret;
    }

    protected abstract <V> Collection<V> createNew(final int length, final Type elementType) throws SerializeException;
}
