/*
 * Copyright 2020-2022 Conelux
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

package org.conelux.conversion.converter;

import io.leangen.geantyref.GenericTypeReflector;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.conelux.conversion.ConversionBus;
import org.conelux.conversion.exception.ConversionException;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"ClassCanBeRecord"})
public class MapToMapConverter implements ConditionalConverter<Map<Object, Object>, Map<Object, Object>> {

    private final ConversionBus conversionBus;

    public MapToMapConverter(ConversionBus conversionBus) {
        this.conversionBus = conversionBus;
    }

    @Override
    public boolean matches(Type sourceType, Type targetType) {
        Type[] sourceParams = this.getParameterTypes(sourceType);
        Type[] targetParams = this.getParameterTypes(targetType);

        if (sourceParams == null || targetParams == null) {
            return false;
        }

        return this.conversionBus.canConvert(sourceParams[0], targetParams[0])
            && this.conversionBus.canConvert(sourceParams[1], targetParams[1]);
    }

    @Override
    public @NotNull Map<Object, Object> convert(@NotNull Map<Object, Object> source, @NotNull Type sourceType,
        @NotNull Type targetType)
        throws ConversionException {

        // Shortcut if possible...
        boolean copyRequired = !GenericTypeReflector.erase(targetType).isInstance(source);
        if (!copyRequired && source.isEmpty()) {
            return source;
        }

        Type[] sourceParams = this.getParameterTypes(sourceType);
        Type[] targetParams = this.getParameterTypes(targetType);
        List<Map.Entry<Object, Object>> targetEntries = new ArrayList<>(source.size());

        for (Map.Entry<Object, Object> entry : source.entrySet()) {
            Object sourceKey = entry.getKey();
            Object sourceValue = entry.getValue();
            Object targetKey = this.conversionBus.convert(sourceKey, sourceParams[0], targetParams[0]);
            Object targetValue = this.conversionBus.convert(targetKey, sourceParams[0], targetParams[1]);
            targetEntries.add(new AbstractMap.SimpleEntry<>(targetKey, targetValue));

            if (sourceKey != targetKey || sourceValue != targetValue) {
                copyRequired = true;
            }
        }
        if (!copyRequired) {
            return source;
        }

        Map<Object, Object> targetMap = new HashMap<>(targetEntries.size());

        for (Map.Entry<Object, Object> entry : targetEntries) {
            targetMap.put(entry.getKey(), entry.getValue());
        }

        return targetMap;

    }

    private Type[] getParameterTypes(Type type) {
        if (type instanceof ParameterizedType parameterizedType) {
            Type[] typeArgs = parameterizedType.getActualTypeArguments();
            if (typeArgs.length != 2) {
                return null;
            }

            return typeArgs;
        }

        return null;
    }
}
