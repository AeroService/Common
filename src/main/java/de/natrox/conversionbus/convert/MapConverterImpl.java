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

package de.natrox.conversionbus.convert;

import de.natrox.common.validate.Check;
import de.natrox.conversionbus.ConversionBus;
import de.natrox.conversionbus.exception.ConversionFailedException;
import de.natrox.conversionbus.exception.SerializeException;
import io.leangen.geantyref.GenericTypeReflector;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("ClassCanBeRecord")
final class MapConverterImpl<T, U> implements MapConverter<T, U> {

    private final Converter<Object, T> keyConverter;
    private final Converter<Object, U> valueConverter;

    private MapConverterImpl(Converter<Object, T> keyConverter, Converter<Object, U> valueConverter) {
        this.keyConverter = keyConverter;
        this.valueConverter = valueConverter;
    }

    static <T, U> MapConverter<T, U> create(Type type, ConversionBus collection) {
        Check.notNull(type, "type");
        Check.notNull(collection, "collection");
        Check.argCondition(!(type instanceof ParameterizedType), "Raw types are not supported for maps");

        Type[] typeArgs = ((ParameterizedType) type).getActualTypeArguments();
        Check.argCondition(typeArgs.length != 2, "Map expected two type arguments");

        Type keyType = typeArgs[0];
        Converter<Object, T> keyConverter = collection.get(keyType);

        Type valueType = typeArgs[1];
        Converter<Object, U> valueConverter = collection.get(valueType);

        return new MapConverterImpl<>(keyConverter, valueConverter);
    }

    @Override
    public @NotNull Map<T, U> read(@NotNull Object obj) throws SerializeException {
        if (obj instanceof Map<?, ?> map) {
            final Map<T, U> ret = new LinkedHashMap<>();
            for (Map.Entry<?, ?> ent : map.entrySet()) {
                ret.put(this.keyConverter.read(ent.getKey()), this.valueConverter.read(ent.getValue()));
            }
            return ret;
        }

        throw new ConversionFailedException(obj, "Map");
    }

    @Override
    public @NotNull Object write(@NotNull Map<T, U> mapValue) throws SerializeException {
        final Map<Object, Object> ret = new LinkedHashMap<>();
        for (Map.Entry<T, U> ent : mapValue.entrySet()) {
            ret.put(this.keyConverter.write(ent.getKey()), this.valueConverter.write(ent.getValue()));
        }
        return ret;
    }
}
