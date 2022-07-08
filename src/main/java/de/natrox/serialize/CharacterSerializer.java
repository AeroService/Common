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

import de.natrox.serialize.exception.SerializeProcessException;
import de.natrox.serialize.preferences.SerializerPreferences;
import de.natrox.serialize.util.NumberUtil;

public final class CharacterSerializer {

    private CharacterSerializer() {
        throw new UnsupportedOperationException();
    }

    public static Object serialize(Character value, SerializerPreferences preferences) {
        for (Class<?> acceptedType : preferences.acceptedTypes()) {
            if (Character.class.isAssignableFrom(acceptedType))
                return value;
            else if (Number.class.isAssignableFrom(acceptedType))
                return serializeToNumber(value).byteValue();
            else if (CharSequence.class.isAssignableFrom(acceptedType))
                return serializeToText(value);
        }
        throw new SerializeProcessException("Unable to find matching type to serialize in preferences.");
    }

    public static Character deserialize(Object value, SerializerPreferences preferences) {
        if (Number.class.isAssignableFrom(value.getClass()))
            return deserializeFromNumber((Number) value);
        if (CharSequence.class.isAssignableFrom(value.getClass())) {
            CharSequence sequence = (CharSequence) value;
            if (sequence.isEmpty())
                throw new SerializeProcessException("Unable to extract character out of an empty char sequence.");
            if (!preferences.lenient() && sequence.length() > 1)
                throw new SerializeProcessException("Unable to extract first character out of an longer char sequence.");
            return deserializeFromText(sequence);
        }
        throw new SerializeProcessException("Unable to cast to an deserializable type for character.");
    }


    private static Number serializeToNumber(final Character value) {
        return (int) value;
    }

    private static CharSequence serializeToText(Character value) {
        return value.toString();
    }

    private static Character deserializeFromNumber(Number number) {
        return (char) (int) NumberUtil.cast(number, int.class);
    }

    private static Character deserializeFromText(CharSequence text) {
        return text.charAt(0);
    }
}
