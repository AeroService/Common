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

package org.conelux.conversion;

import io.leangen.geantyref.TypeToken;
import java.lang.reflect.Type;
import org.conelux.conversion.exception.ConversionException;
import org.jetbrains.annotations.NotNull;

public sealed interface ConversionBus extends ConverterRegistry permits ConversionBusImpl {

    static @NotNull ConversionBus create() {
        return new ConversionBusImpl();
    }

    static @NotNull ConversionBus createDefault() {
        return new DefaultConversionBus();
    }

    boolean canConvert(@NotNull Type sourceType, @NotNull Type targetType);

    default boolean canConvert(@NotNull TypeToken<?> sourceTypeToken, @NotNull TypeToken<?> targetTypeToken) {
        return this.canConvert(sourceTypeToken.getType(), targetTypeToken.getType());
    }

    @NotNull Object convert(@NotNull Object source, @NotNull Type sourceType, @NotNull Type targetType) throws ConversionException;

    @SuppressWarnings("unchecked")
    default <T> @NotNull T convert(@NotNull Object source, @NotNull Class<T> targetType) throws ConversionException {
        return (T) this.convert(source, source.getClass(), targetType);
    }

    @SuppressWarnings("unchecked")
    default <T, U> @NotNull T convert(@NotNull U source, @NotNull TypeToken<U> sourceTypeToken, @NotNull TypeToken<T> targetTypeToken)
        throws ConversionException {
        return (T) this.convert(source, sourceTypeToken.getType(), targetTypeToken.getType());
    }
}
