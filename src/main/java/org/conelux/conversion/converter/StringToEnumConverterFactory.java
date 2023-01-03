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

import org.conelux.conversion.exception.ConversionException;
import org.conelux.conversion.exception.ConversionFailedException;
import org.conelux.conversion.util.ConversionUtil;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"rawtypes", "unchecked"})
public class StringToEnumConverterFactory implements ConverterFactory<String, Enum> {

	@Override
	public <T extends Enum> Converter<String, T> create(Class<T> targetType) {
		return new StringToEnum(ConversionUtil.getEnumType(targetType));
	}

	@SuppressWarnings("ClassCanBeRecord")
    private static class StringToEnum<T extends Enum> implements Converter<String, T> {

		private final Class<T> enumType;

		StringToEnum(Class<T> enumType) {
			this.enumType = enumType;
		}

		@Override
		public @NotNull T convert(@NotNull String source, @NotNull Class<String> sourceType, @NotNull Class<T> targetType) throws ConversionException {
			if (source.isEmpty()) {
				// It's an empty enum identifier: reset the enum value to null.
				throw new ConversionFailedException(source, "");
			}
			return (T) Enum.valueOf(this.enumType, source.trim());
		}
	}

}
