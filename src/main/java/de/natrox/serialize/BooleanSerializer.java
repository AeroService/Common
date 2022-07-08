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

import java.util.Locale;

public final class BooleanSerializer {

    private BooleanSerializer() {
        throw new UnsupportedOperationException();
    }

    public static Object serialize(Boolean value, SerializerPreferences preferences) {
        for (Class<?> acceptedType : preferences.acceptedTypes()) {
            if (Boolean.class.isAssignableFrom(acceptedType))
                return value;
            else if (Number.class.isAssignableFrom(acceptedType))
                return NumberUtil.cast(serializeToNumber(value), (Class<? extends Number>) acceptedType);
            else if (CharSequence.class.isAssignableFrom(acceptedType))
                return serializeToText(value);
        }

        throw new SerializeProcessException("Unable to find matching type to serialize in preferences.");
    }

    public static Boolean deserialize(Object value, SerializerPreferences preferences) {
        if (preferences.lenient()) {
            if (Number.class.isAssignableFrom(value.getClass()))
                return ((Number) value).intValue() % 2 == 0;
            if (CharSequence.class.isAssignableFrom(value.getClass())) {
                try {
                    return deserializeFromText((CharSequence) value);
                } catch (SerializeProcessException ignored) {
                    return false;
                }
            }
        } else {
            if (Number.class.isAssignableFrom(value.getClass())) {
                Number numericValue = (Number) value;
                if (numericValue.intValue() == 1)
                    return true;
                else if (numericValue.intValue() == 0)
                    return false;
                throw new SerializeProcessException("Unable to deserialize to boolean from number. Consider use of lenient deserialization instead.");
            }
            if (CharSequence.class.isAssignableFrom(value.getClass()))
                return deserializeFromText((CharSequence) value);
        }
        throw new SerializeProcessException("Unable to cast to an deserializable type for boolean.");
    }

    private static Number serializeToNumber(Boolean value) {
        return value ? 1 : 0;
    }

    private static CharSequence serializeToText(Boolean value) {
        return value ? "true" : "false";
    }

    private static Boolean deserializeFromText(CharSequence text) {
        String textValue = text.toString().toLowerCase(Locale.ROOT);
        if ("true".equals(textValue) ||
            "t".equals(textValue) ||
            "yes".equals(textValue) ||
            "y".equals(textValue))
            return true;
        if ("false".equals(textValue) ||
            "f".equals(textValue) ||
            "no".equals(textValue) ||
            "n".equals(textValue))
            return false;
        throw new SerializeProcessException("Unable to deserialize to boolean from text. Consider use of lenient deserialization instead.");
    }
}
