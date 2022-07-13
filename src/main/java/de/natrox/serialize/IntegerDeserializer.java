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

final class IntegerDeserializer extends NumericDeserializer<Integer> {

    IntegerDeserializer(Class<Integer> type) {
        super(type);
    }

    @Override
    public @NotNull Integer deserialize(@NotNull Object obj, @NotNull Type type) throws SerializeException {
        if (obj instanceof Float || obj instanceof Double) {
            double absValue = Math.abs(((Number) obj).doubleValue());
            if ((absValue - Math.floor(absValue)) < EPSILON && absValue <= Integer.MAX_VALUE) {
                return (int) absValue;
            }
        }

        if (obj instanceof Number) {
            final long full = ((Number) obj).longValue();
            if (full > Integer.MAX_VALUE || full < Integer.MIN_VALUE) {
                throw new SerializeException("Value " + full + " is out of range for an integer ([" + Integer.MIN_VALUE + "," + Integer.MAX_VALUE + "])");
            }
            return (int) full;
        }

        if (obj instanceof CharSequence) {
            return parseNumber(obj.toString(), Integer::parseInt, Integer::parseUnsignedInt, "i");
        }

        throw new CoercionFailedException(obj, "int");
    }
}
