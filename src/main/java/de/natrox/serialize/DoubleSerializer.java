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

final class DoubleSerializer extends NumericSerializer<Double> {

    DoubleSerializer(Class<Double> type) {
        super(type);
    }

    @Override
    public @NotNull Double deserialize(@NotNull Object obj, @NotNull Type type) throws SerializeException {
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
        throw new CoercionFailedException(obj, "double");
    }
}
