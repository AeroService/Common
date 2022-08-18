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

package de.natrox.conversion.convert;

import de.natrox.conversion.exception.ConversionFailedException;
import de.natrox.conversion.exception.SerializeException;
import org.jetbrains.annotations.NotNull;

final class CharConverterImpl implements Converter<Object, Character> {

    @Override
    public @NotNull Character read(@NotNull Object obj) throws SerializeException {
        if (obj instanceof Character charValue) {
            return charValue;
        }

        if (obj instanceof CharSequence charSequence) {
            if (charSequence.length() == 1) {
                return charSequence.charAt(0);
            }
            throw new SerializeException("Only single character expected, but received " + charSequence);
        }

        if (obj instanceof Number numValue) {
            return (char) numValue.shortValue();
        }

        throw new ConversionFailedException(obj, "char");
    }
}
