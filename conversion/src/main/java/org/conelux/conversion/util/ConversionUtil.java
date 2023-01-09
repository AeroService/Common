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

package org.conelux.conversion.util;

import org.conelux.common.core.validate.Check;
import org.jetbrains.annotations.NotNull;

public final class ConversionUtil {

    private ConversionUtil() {

    }

    public static Class<?> getEnumType(@NotNull Class<?> targetType) {
        Class<?> enumType = targetType;
        while (enumType != null && !enumType.isEnum()) {
            enumType = enumType.getSuperclass();
        }

        Check.notNull(enumType, "The target type " + targetType.getName() + " does not refer to an enum");
        return enumType;
    }

}
