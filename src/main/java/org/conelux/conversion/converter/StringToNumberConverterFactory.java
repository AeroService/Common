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
import java.math.BigDecimal;
import java.math.BigInteger;
import org.conelux.conversion.exception.ConversionException;
import org.conelux.conversion.exception.ConversionFailedException;
import org.jetbrains.annotations.NotNull;

public class StringToNumberConverterFactory implements ConverterFactory<String, Number> {

	@Override
	public <T extends Number> Converter<String, T> create(Class<T> targetType) {
		return new StringToNumber<>(targetType);
	}

	@SuppressWarnings("ClassCanBeRecord")
    private static final class StringToNumber<T extends Number> implements Converter<String, T> {

		private final Class<T> targetType;

		public StringToNumber(Class<T> targetType) {
			this.targetType = targetType;
		}

		@SuppressWarnings("unchecked")
        @Override
		public @NotNull T convert(@NotNull String source, @NotNull Type sourceType, @NotNull Type targetType) throws ConversionException {
			if (source.isEmpty()) {
				throw new ConversionFailedException(sourceType, targetType);
			}
            String trimmed = source.trim();

            if (Byte.class == this.targetType) {
                return (T) Byte.valueOf(trimmed);
            } else if (Short.class == this.targetType) {
                return (T) Short.valueOf(trimmed);
            } else if (Integer.class == this.targetType) {
                return (T) Integer.valueOf(trimmed);
            } else if (Long.class == this.targetType) {
                return (T) Long.valueOf(trimmed);
            } else if (BigInteger.class == this.targetType) {
                return (T) new BigInteger(trimmed);
            } else if (Float.class == this.targetType) {
                return (T) Float.valueOf(trimmed);
            } else if (Double.class == this.targetType) {
                return (T) Double.valueOf(trimmed);
            } else if (BigDecimal.class == this.targetType || Number.class == this.targetType) {
                return (T) new BigDecimal(trimmed);
            }

            throw new ConversionFailedException(sourceType, targetType);
		}
	}
}
