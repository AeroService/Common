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
import java.util.UUID;

final class UuidDeserializer extends TypeDeserializer<UUID>  {

    private static final char DASH = '-';

    UuidDeserializer() {
        super(UUID.class);
    }

    @Override
    public @NotNull UUID deserialize(@NotNull Object obj, @NotNull Type type) throws SerializeException {
        if (obj instanceof final long[] arr) {
            if (arr.length == 2) { // big-endian, cuz we're java
                return new UUID(arr[0], arr[1]);
            }
        }
        String uuidStr = obj.toString();
        if (uuidStr.length() == 32) { // Mojang-style, without dashes
            uuidStr = uuidStr.substring(0, 8) + DASH +
                uuidStr.substring(8, 12) + DASH +
                uuidStr.substring(12, 16) + DASH +
                uuidStr.substring(16, 20) + DASH +
                uuidStr.substring(20, 32);
        }
        try {
            return UUID.fromString(uuidStr);
        } catch (final IllegalArgumentException ex) {
            throw new CoercionFailedException(obj, "UUID");
        }
    }
}
