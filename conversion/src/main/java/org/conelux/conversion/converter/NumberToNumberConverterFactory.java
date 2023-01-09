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

public class NumberToNumberConverterFactory implements ConverterFactory<Number, Number>, ConverterCondition {

    @Override
    public <T extends Number> Converter<Number, T> create(Class<T> targetType) {
        return new NumberToNumber<>(targetType);
    }

    @Override
    public boolean matches(Type sourceType, Type targetType) {
        return !sourceType.equals(targetType);
    }

    @SuppressWarnings("ClassCanBeRecord")
    private static final class NumberToNumber<T extends Number> implements Converter<Number, T> {

        private static final BigInteger LONG_MIN = BigInteger.valueOf(Long.MIN_VALUE);
        private static final BigInteger LONG_MAX = BigInteger.valueOf(Long.MAX_VALUE);

        private final Class<T> targetType;

        NumberToNumber(Class<T> targetType) {
            this.targetType = targetType;
        }

        @SuppressWarnings("unchecked")
        @Override
        public @NotNull T convert(@NotNull Number source, @NotNull Type sourceType, @NotNull Type targetType)
            throws ConversionException {
            long value = this.convertToLong(source, sourceType, targetType);
            if (Byte.class == this.targetType) {
                if (value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE) {
                    return (T) Byte.valueOf(source.byteValue());
                }
            } else if (Short.class == this.targetType) {
                if (value >= Short.MIN_VALUE && value <= Short.MAX_VALUE) {
                    return (T) Short.valueOf(source.shortValue());
                }
            } else if (Integer.class == this.targetType) {
                if (value >= Integer.MIN_VALUE && value <= Integer.MAX_VALUE) {
                    return (T) Integer.valueOf(source.intValue());
                }
            } else if (Long.class == this.targetType) {
                return (T) Long.valueOf(value);
            } else if (BigInteger.class == this.targetType) {
                if (source instanceof BigDecimal bigDecimal) {
                    return (T) bigDecimal.toBigInteger();
                } else {
                    return (T) BigInteger.valueOf(source.longValue());
                }
            } else if (Float.class == this.targetType) {
                return (T) Float.valueOf(source.floatValue());
            } else if (Double.class == this.targetType) {
                return (T) Double.valueOf(source.doubleValue());
            } else if (BigDecimal.class == this.targetType) {
                return (T) new BigDecimal(source.toString());
            }

            throw new ConversionFailedException(sourceType, targetType);
        }

        private long convertToLong(Number source, Type sourceType, Type targetType) throws ConversionException {
            BigInteger bigInt = source instanceof BigInteger bigInteger ? bigInteger
                : source instanceof BigDecimal bigDecimal ? bigDecimal.toBigInteger() : null;

            if (bigInt == null || (bigInt.compareTo(LONG_MIN) >= 0 && bigInt.compareTo(LONG_MAX) <= 0)) {
                return source.longValue();
            }

            throw new ConversionFailedException(sourceType, targetType);
        }
    }
}
