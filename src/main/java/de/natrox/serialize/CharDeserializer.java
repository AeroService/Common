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

final class CharDeserializer extends TypeDeserializer<Character> {

    CharDeserializer(Class<Character> type) {
        super(type);
    }

    @Override
    public @NotNull Character deserialize(@NotNull Object obj, @NotNull Type type) throws SerializeException {
        if (obj instanceof final String strValue) {
            if (strValue.length() == 1) {
                return strValue.charAt(0);
            }
            throw new SerializeException(type, "Only single character expected, but received " + strValue);
        } else if (obj instanceof Number numValue) {
            return (char) numValue.shortValue();
        }

        throw new CoercionFailedException(type, obj, "char");
    }
}
