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

package de.natrox.conversion.converter;

import de.natrox.conversion.exception.ConversionFailedException;
import de.natrox.conversion.exception.ConversionException;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Set;

public class ObjectToBooleanConverter implements Converter<Object, Boolean> {

    private static final Set<String> TRUE_VALUES = Set.of("true", "t", "on", "yes", "y", "1");
    private static final Set<String> FALSE_VALUES = Set.of("false", "f", "off", "no", "n", "0");

    @Override
    public @NotNull Boolean convert(@NotNull Object source) throws ConversionException {
        String value = source.toString().trim().toLowerCase(Locale.ROOT);

        if (TRUE_VALUES.contains(value)) {
            return Boolean.TRUE;
        }

        if (FALSE_VALUES.contains(value)) {
            return Boolean.FALSE;
        }

        throw new ConversionFailedException(source, "boolean");
    }
}
