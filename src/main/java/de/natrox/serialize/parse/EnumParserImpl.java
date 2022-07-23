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

package de.natrox.serialize.parse;

import de.natrox.serialize.exception.CoercionFailedException;
import de.natrox.serialize.exception.SerializeException;
import io.leangen.geantyref.GenericTypeReflector;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.*;

@SuppressWarnings("unchecked")
final class EnumParserImpl<T extends Enum<T>> implements EnumParser<T> {

    private static final Map<Class<? extends Enum<?>>, Map<String, Enum<?>>> ENUM_FIELD_CACHE = new WeakHashMap<>();
    private final Type type;

    EnumParserImpl(Type type) {
        this.type = type;
    }

    @Override
    public @NotNull T parse(@NotNull Object obj) throws SerializeException {
        String enumConstant = obj.toString();
        T ret = lookupEnum(enumConstant);
        if (ret == null) {
            throw new CoercionFailedException(this.type, "Invalid enum constant provided, expected a value of enum, got " + enumConstant);
        }
        return ret;
    }

    private T lookupEnum(String key) {
        Map<String, Enum<?>> vals = ENUM_FIELD_CACHE.computeIfAbsent((Class<? extends Enum<?>>) GenericTypeReflector.erase(this.type).asSubclass(Enum.class), c2 -> {
            Map<String, Enum<?>> ret = new HashMap<>();
            for (Enum<?> field : c2.getEnumConstants()) {
                ret.put(field.name(), field);
                ret.putIfAbsent(processKey(field.name()), field);
            }
            return Collections.unmodifiableMap(ret);
        });


        Enum<?> possibleRet = vals.get(key);
        if (possibleRet != null) {
            return (T) possibleRet;
        }
        return (T) vals.get(processKey(key));
    }

    private String processKey(final String key) {
        // stick a flower at the front so processed keys are different from literal keys
        return "🌸" + key.toLowerCase(Locale.ROOT).replace("_", "");
    }
}
