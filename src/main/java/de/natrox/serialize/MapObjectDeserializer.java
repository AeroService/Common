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
import de.natrox.serialize.objectmapping.ObjectMapper;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.Map;

@SuppressWarnings({"unchecked", "ClassCanBeRecord"})
public class MapObjectDeserializer<T> implements SpecificDeserializer<T, Map<String, Object>> {

    private final ObjectMapper.Factory objectMapperFactory;

    public MapObjectDeserializer(ObjectMapper.Factory objectMapperFactory) {
        this.objectMapperFactory = objectMapperFactory;
    }

    @Override
    public @NotNull T deserialize(@NotNull Map<String, Object> obj, @NotNull Type type) throws SerializeException {
        return (T) this.objectMapperFactory.get(type).load(obj);
    }
}
