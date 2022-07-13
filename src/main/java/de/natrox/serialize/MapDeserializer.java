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

import de.natrox.serialize.exception.SerializeException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

final class MapDeserializer implements Deserializer<Map<?, ?>> {

    @Override
    public @NotNull Map<?, ?> deserialize(@NotNull Object obj, @NotNull Type type) throws SerializeException {
        Map<Object, Object> ret = new LinkedHashMap<>();
        if (obj instanceof Map<?, ?> map) {
            if (!(type instanceof final ParameterizedType param)) {
                throw new SerializeException(type, "Raw types are not supported for collections");
            }
            Type[] typeArgs = param.getActualTypeArguments();
            if (typeArgs.length != 2) {
                throw new SerializeException(type, "Map expected two type arguments!");
            }
            Type key = typeArgs[0];
            Type value = typeArgs[1];
            @Nullable SpecificDeserializer<?, Object> keySerial = (SpecificDeserializer<?, Object>) SerializerCollection.defaults().get(key);
            @Nullable SpecificDeserializer<?, Object> valueSerial = (SpecificDeserializer<?, Object>) SerializerCollection.defaults().get(value);

            if (keySerial == null) {
                throw new SerializeException(type, "No type serializer available for key type " + key);
            }

            if (valueSerial == null) {
                throw new SerializeException(type, "No type serializer available for value type " + value);
            }

            for (final Map.Entry<?, ?> ent : map.entrySet()) {
                ret.put(keySerial.deserialize(ent.getKey(), key), valueSerial.deserialize(ent.getValue(), value));
            }
        }
        return ret;
    }
}
