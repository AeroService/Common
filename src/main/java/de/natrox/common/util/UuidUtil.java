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

package de.natrox.common.util;

import de.natrox.common.validate.Check;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Represents utility class that hols some methods to work with uuids.
 */
public final class UuidUtil {

    private UuidUtil() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the {@link UUID} of a string.
     *
     * @param name the string for the generation
     * @return the uuid
     */
    public static @NotNull UUID fromName(@NotNull String name) {
        Check.notNull(name, "name");
        return UUID.nameUUIDFromBytes(name.toLowerCase().getBytes(StandardCharsets.UTF_8));
    }
}
