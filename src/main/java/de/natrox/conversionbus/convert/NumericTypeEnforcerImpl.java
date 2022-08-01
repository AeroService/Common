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

import de.natrox.conversionbus.exception.ConversionFailedException;
import de.natrox.conversionbus.exception.SerializeException;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.function.BiFunction;

final class NumericTypeEnforcerImpl {

    final static class ByteConverter extends AbstractNumericConverter<Byte> {

        @Override
        public @NotNull Byte read(@NotNull Object obj) throws SerializeException {
            if (obj instanceof Byte byteValue) {
                return byteValue;
            }

            if (obj instanceof Float || obj instanceof Double) {
                double absValue = Math.abs(((Number) obj).doubleValue());
                if ((absValue - Math.floor(absValue)) < EPSILON && absValue <= Byte.MAX_VALUE) {
                    return (byte) absValue;
                }
            }

            if (obj instanceof Number) {
                long full = ((Number) obj).longValue();
                if (full > Byte.MAX_VALUE || full < Byte.MIN_VALUE) {
                    throw new SerializeException("Value " + full + " is out of range for a byte ([" + Byte.MIN_VALUE + "," + Byte.MAX_VALUE + "])");
                }
                return (byte) full;
            }

            if (obj instanceof CharSequence) {
                return AbstractNumericConverter.parseNumber(obj.toString(), Byte::parseByte, Byte::parseByte, "b");
            }

            throw new ConversionFailedException(obj, "byte");
        }
    }

    final static class DoubleConverter extends AbstractNumericConverter<Double> {

        @Override
        public @NotNull Double read(@NotNull Object obj) throws SerializeException {
            if (obj instanceof Number) {
                return ((Number) obj).doubleValue();
            } else if (obj instanceof CharSequence) {
                String stringValue = obj.toString();
                if (stringValue.endsWith("d") || stringValue.endsWith("D")) {
                    stringValue = stringValue.substring(0, stringValue.length() - 1);
                }
                try {
                    return Double.parseDouble(stringValue);
                } catch (NumberFormatException exception) {
                    throw new SerializeException(exception);
                }
            }
            throw new ConversionFailedException(obj, "double");
        }
    }

    final static class FloatConverter extends AbstractNumericConverter<Float> {

        @Override
        public @NotNull Float read(@NotNull Object obj) throws SerializeException {
            if (obj instanceof Number) {
                double doubleValue = ((Number) obj).doubleValue();
                if (!this.canRepresentDoubleAsFloat(doubleValue)) {
                    throw new SerializeException("Value " + doubleValue + " cannot be represented as a float without significant loss of precision");
                }
                return (float) doubleValue;
            } else if (obj instanceof CharSequence) {
                String stringValue = obj.toString();
                if (stringValue.endsWith("f") || stringValue.endsWith("F")) {
                    stringValue = stringValue.substring(0, stringValue.length() - 1);
                }

                try {
                    return Float.parseFloat(stringValue);
                } catch (NumberFormatException exception) {
                    throw new SerializeException(exception);
                }
            }

            throw new ConversionFailedException(obj, "float");
        }

        private boolean canRepresentDoubleAsFloat(double test) {
            if (test == 0d || !Double.isFinite(test)) { // NaN, +/-inf
                return true;
            }

            int exponent = Math.getExponent(test);
            return exponent >= Float.MIN_EXPONENT && exponent <= Float.MAX_EXPONENT;
        }
    }

    final static class IntegerConverter extends AbstractNumericConverter<Integer> {

        @Override
        public @NotNull Integer read(@NotNull Object obj) throws SerializeException {
            if (obj instanceof Float || obj instanceof Double) {
                double absValue = Math.abs(((Number) obj).doubleValue());
                if ((absValue - Math.floor(absValue)) < EPSILON && absValue <= Integer.MAX_VALUE) {
                    return (int) absValue;
                }
            }

            if (obj instanceof Number) {
                long full = ((Number) obj).longValue();
                if (full > Integer.MAX_VALUE || full < Integer.MIN_VALUE) {
                    throw new SerializeException("Value " + full + " is out of range for an integer ([" + Integer.MIN_VALUE + "," + Integer.MAX_VALUE + "])");
                }
                return (int) full;
            }

            if (obj instanceof CharSequence) {
                return AbstractNumericConverter.parseNumber(obj.toString(), Integer::parseInt, Integer::parseUnsignedInt, "i");
            }

            throw new ConversionFailedException(obj, "int");
        }
    }

    final static class LongConverter extends AbstractNumericConverter<Long> {

        @Override
        public @NotNull Long read(@NotNull Object obj) throws SerializeException {
            if (obj instanceof Float || obj instanceof Double) {
                double absValue = Math.abs(((Number) obj).doubleValue());
                if ((absValue - Math.floor(absValue)) < EPSILON && absValue <= Long.MAX_VALUE) {
                    return (long) absValue;
                }
            }

            if (obj instanceof Number) {
                return ((Number) obj).longValue();
            }

            if (obj instanceof CharSequence) {
                return AbstractNumericConverter.parseNumber(obj.toString(), Long::parseLong, Long::parseUnsignedLong, "l");
            }

            throw new ConversionFailedException(obj, "long");
        }
    }

    final static class ShortConverter extends AbstractNumericConverter<Short> {

        @Override
        public @NotNull Short read(@NotNull Object obj) throws SerializeException {
            if (obj instanceof Float || obj instanceof Double) {
                double absValue = Math.abs(((Number) obj).doubleValue());
                if ((absValue - Math.floor(absValue)) < EPSILON && absValue <= Short.MAX_VALUE) {
                    return (short) absValue;
                }
            }

            if (obj instanceof Number) {
                long full = ((Number) obj).longValue();
                if (full > Short.MAX_VALUE || full < Short.MIN_VALUE) {
                    throw new SerializeException("Value " + full + " is out of range for a short ([" + Short.MIN_VALUE + "," + Short.MAX_VALUE + "])");
                }
                return (short) full;
            }

            if (obj instanceof CharSequence) {
                return AbstractNumericConverter.parseNumber(obj.toString(), Short::parseShort, Short::parseShort, "s");
            }

            throw new ConversionFailedException(obj, "short");
        }
    }

    private static abstract class AbstractNumericConverter<T> implements Converter<Object, T> {

        protected final static float EPSILON = Float.MIN_NORMAL;

        static <T extends Number> T parseNumber(String input, BiFunction<String, Integer, T> parseFunc, BiFunction<String, Integer, T> unsignedParseFunc, String suffix) throws SerializeException {
            input = input.toLowerCase();

            boolean unsigned = false;
            boolean negative = false;

            int startIdx = 0;
            int endIdx = input.length();

            // type suffix
            if (input.endsWith(suffix) || input.endsWith(suffix.toUpperCase(Locale.ROOT))) {
                --endIdx;
            }

            // unsigned
            if (endIdx > 0 && input.charAt(endIdx - 1) == 'u') {
                unsigned = true;
                --endIdx;
            }

            if (endIdx > startIdx && input.charAt(startIdx) == '-') {
                if (unsigned) {
                    throw new SerializeException("Negative numbers cannot be unsigned! (both - prefix and u suffix were used)");
                }
                negative = true;
                ++startIdx;
            } else if (endIdx > startIdx && input.charAt(startIdx) == '+') { // skip, positive is the default
                ++startIdx;
            }

            // bases
            int radix = 10;
            if (input.startsWith("0x", startIdx)) { // hex
                radix = 16;
                startIdx += 2;
            } else if (input.length() > startIdx && input.charAt(startIdx) == '#') { // hex
                radix = 16;
                ++startIdx;
            } else if (input.startsWith("0b", startIdx)) { // binary
                radix = 2;
                startIdx += 2;
            }

            input = input.substring(startIdx, endIdx);

            if (negative) { // ugly but not super avoidable without knowing the number type
                input = "-" + input;
            }
            try {
                return (unsigned ? unsignedParseFunc : parseFunc).apply(input, radix);
            } catch (IllegalArgumentException ex) {
                throw new SerializeException(ex);
            }
        }
    }
}
