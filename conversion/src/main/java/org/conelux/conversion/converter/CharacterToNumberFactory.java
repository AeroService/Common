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

import java.lang.reflect.Type;
import org.conelux.conversion.ConversionBus;
import org.conelux.conversion.exception.ConversionException;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("ClassCanBeRecord")
public class CharacterToNumberFactory implements ConverterFactory<Character, Number> {

    private final ConversionBus conversionBus;

    public CharacterToNumberFactory(ConversionBus conversionBus) {
        this.conversionBus = conversionBus;
    }

    @Override
    public <T extends Number> Converter<Character, T> create(Class<T> targetType) {
        return new CharacterToNumber<>(this.conversionBus, targetType);
    }

    @SuppressWarnings("ClassCanBeRecord")
    private static final class CharacterToNumber<T extends Number> implements Converter<Character, T> {

        private final ConversionBus conversionBus;
        private final Class<T> targetType;

        public CharacterToNumber(ConversionBus conversionBus, Class<T> targetType) {
            this.conversionBus = conversionBus;
            this.targetType = targetType;
        }

        @Override
        public @NotNull T convert(@NotNull Character source, @NotNull Type sourceType, @NotNull Type targetType)
            throws ConversionException {
            return this.conversionBus.convert((short) source.charValue(), this.targetType);
        }
    }

}
