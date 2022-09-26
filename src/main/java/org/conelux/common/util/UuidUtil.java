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

package org.conelux.common.util;

import org.conelux.common.validate.Check;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

/**
 * Represents utility class that holds some methods to work with uuids.
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