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

package de.natrox.serialize;

import de.natrox.serialize.exception.CoercionFailedException;
import de.natrox.serialize.exception.SerializeException;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

final class FloatDeserializer extends NumericDeserializer<Float> {

    FloatDeserializer(Class<Float> type) {
        super(type);
    }

    private static boolean canRepresentDoubleAsFloat(double test) {
        if (test == 0d || !Double.isFinite(test)) { // NaN, +/-inf
            return true;
        }

        int exponent = Math.getExponent(test);
        return exponent >= Float.MIN_EXPONENT && exponent <= Float.MAX_EXPONENT;
    }

    @Override
    public @NotNull Float deserialize(@NotNull Object obj, @NotNull Type type) throws SerializeException {
        if (obj instanceof Number) {
            double doubleValue = ((Number) obj).doubleValue();
            if (!canRepresentDoubleAsFloat(doubleValue)) {
                throw new SerializeException("Value " + doubleValue + " cannot be represented as a float without significant loss of precision");
            }
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

        throw new CoercionFailedException(obj, "float");
    }
}
