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

package org.conelux.conversion;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.conelux.common.validate.Check;
import org.conelux.conversion.converter.ConditionalConverter;
import org.conelux.conversion.converter.Converter;
import org.conelux.conversion.converter.ConverterCondition;
import org.conelux.conversion.converter.ConverterFactory;
import org.conelux.conversion.exception.ConversionException;
import org.conelux.conversion.exception.ConverterNotFoundException;
import org.conelux.conversion.util.ConversionUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

@SuppressWarnings({"unchecked"})
final class ConversionBusImpl implements ConversionBus {

    private final Set<ConditionalConverter<Object, Object>> converters;
    private final Map<Key, ConditionalConverter<Object, Object>> cache = new ConcurrentHashMap<>(64);

    ConversionBusImpl(Set<ConditionalConverter<Object, Object>> serializers) {
        this.converters = Collections.unmodifiableSet(serializers);
    }

    @Override
    public <T> @NotNull T convert(@NotNull Object source, @NotNull Class<T> targetType) throws ConversionException {
        Check.notNull(source, "source");
        Check.notNull(targetType, "targetType");

        Key key = new Key(source.getClass(), targetType);
        ConditionalConverter<Object, Object> converter = this.cache.computeIfAbsent(key, param -> {
            for (ConditionalConverter<Object, Object> conv : this.converters) {
                if (conv.matches(param.sourceType(), param.targetType())) {
                    return conv;
                }
            }

            return null;
        });

        if (converter == null) {
            // No Converter found
            throw new ConverterNotFoundException(source.getClass(), targetType);
        }

        Object result = converter.convert(source, (Class<Object>) source.getClass(), (Class<Object>) targetType);




        return (T) result;
    }

    private static final class ConverterAdapter implements ConditionalConverter<Object, Object> {

        private final Converter<Object, Object> converter;
        private final Class<?> sourceType;
        private final Class<?> targetType;

        public ConverterAdapter(Converter<?, ?> converter, Class<?> sourceType, Class<?> targetType) {
            this.converter = (Converter<Object, Object>) converter;
            this.sourceType = sourceType;
            this.targetType = targetType;
        }

        @Override
        public @NotNull Object convert(@NotNull Object obj, @NotNull Class<Object> sourceType,
            @NotNull Class<Object> targetType) throws ConversionException {
            return this.converter.convert(obj, sourceType, targetType);
        }

        @Override
        public boolean matches(Class<?> sourceType, Class<?> targetType) {
            if (this.targetType != targetType) {
                return false;
            }

            return this.sourceType.isAssignableFrom(sourceType);
        }
    }

    private static final class ConverterFactoryAdapter implements ConditionalConverter<Object, Object> {

        private final ConverterFactory<Object, Object> converterFactory;
        private final Class<?> sourceType;
        private final Class<?> targetType;

        public ConverterFactoryAdapter(ConverterFactory<?, ?> converterFactory, Class<?> sourceType, Class<?> targetType) {
            this.converterFactory = (ConverterFactory<Object, Object>) converterFactory;
            this.sourceType = sourceType;
            this.targetType = targetType;
        }

        @Override
        public @NotNull Object convert(@NotNull Object obj, @NotNull Class<Object> sourceType,
            @NotNull Class<Object> targetType) throws ConversionException {
            return this.converterFactory.create(targetType).convert(obj, sourceType, targetType);
        }

        @Override
        public boolean matches(Class<?> sourceType, Class<?> targetType) {
            if (this.converterFactory instanceof ConverterCondition condition && condition.matches(sourceType, targetType)) {
                Converter<?, ?> converter = this.converterFactory.create(targetType);
                if (converter instanceof ConverterCondition converterCondition) {
                    return converterCondition.matches(sourceType, targetType);
                }
            }

            if (!this.targetType.isAssignableFrom(targetType)) {
                return false;
            }

            return this.sourceType.isAssignableFrom(sourceType);
        }
    }

    record Key(Class<?> sourceType, Class<?> targetType) {

        @Override
        public boolean equals(@Nullable Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Key otherKey)) {
                return false;
            }
            return (this.sourceType.equals(otherKey.sourceType)) && this.targetType.equals(otherKey.targetType);
        }
    }

    final static class BuilderImpl implements ConversionBus.Builder {

        private final Set<ConditionalConverter<Object, Object>> converters = new HashSet<>();

        BuilderImpl() {

        }

        @Override
        public <U, V> Builder register(Class<? extends U> source, Class<V> target, Converter<U, V> converter) {
            this.converters.add(new ConverterAdapter(converter, source, target));
            return this;
        }

        @Override
        public Builder register(ConditionalConverter<?, ?> converter) {
            this.converters.add((ConditionalConverter<Object, Object>) converter);
            return this;
        }

        @Override
        public <U, V> Builder register(Class<? extends U> source, Class<V> target, ConverterFactory<?, ?> converterFactory) {
            this.converters.add(new ConverterFactoryAdapter(converterFactory, source, target));
            return this;
        }

        @Override
        public @UnknownNullability ConversionBus build() {
            return new ConversionBusImpl(this.converters);
        }
    }
}
