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

		@Override
		public @NotNull T convert(@NotNull String source, @NotNull Type sourceType, @NotNull Type targetType) throws ConversionException {
			if (source.isEmpty()) {
				throw new ConversionFailedException(source, "");
			}
			return (T) Integer.valueOf(0); //NumberUtils.parseNumber(source, this.targetType);
		}
	}
}
