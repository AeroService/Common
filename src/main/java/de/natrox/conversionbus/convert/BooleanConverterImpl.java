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

final class BooleanConverterImpl implements Converter<Object, Boolean> {

    @Override
    public @NotNull Boolean read(@NotNull Object obj) throws SerializeException {
        if (obj instanceof Number) {
            return !obj.equals(0);
        }

        String potential = obj.toString().toLowerCase(Locale.ROOT);
        if (potential.equals("true")
            || potential.equals("t")
            || potential.equals("yes")
            || potential.equals("y")
            || potential.equals("1")) {
            return true;
        } else if (potential.equals("false")
            || potential.equals("f")
            || potential.equals("no")
            || potential.equals("n")
            || potential.equals("0")) {
            return false;
        }

        throw new ConversionFailedException(obj, "boolean");
    }
}
