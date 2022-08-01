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

import de.natrox.conversionbus.exception.SerializeException;
import de.natrox.conversionbus.objectmapping.ObjectMapper;
import io.leangen.geantyref.GenericTypeReflector;
import io.leangen.geantyref.TypeToken;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unchecked", "ClassCanBeRecord"})
final class ObjectConverterImpl<T> implements ObjectConverter<T> {

    public static final String CLASS_KEY = "__class__";
    private final Type type;
    private final ObjectMapper.Factory objectMapperFactory;

    ObjectConverterImpl(Type type, ObjectMapper.Factory objectMapperFactory) {
        this.type = type;
        this.objectMapperFactory = objectMapperFactory;
    }

    @Override
    public @NotNull T read(@NotNull Map<String, Object> obj) throws SerializeException {
        Type objType = TypeToken.get(obj.getClass()).getType();

        if (!GenericTypeReflector.isSuperType(Map.class, objType)) {
            throw new SerializeException(this.type, "Only map types are supported");
        }

        final Type clazz = this.instantiableType((String) obj.get(CLASS_KEY));
        return (T) this.objectMapperFactory.get(clazz).load(obj);
    }

    @Override
    public @NotNull Object write(@NotNull T value) throws SerializeException {
        Map<String, Object> mapValue = new HashMap<>();
        Class<?> rawType = GenericTypeReflector.erase(this.type);
        ObjectMapper<?> mapper;
        if (rawType.isInterface() || Modifier.isAbstract(rawType.getModifiers())) {
            mapValue.put(CLASS_KEY, value.getClass().getName());
            mapper = ObjectMapper.factory().get(value.getClass());
        } else {
            mapper = ObjectMapper.factory().get(this.type);
        }
        ((ObjectMapper<Object>) mapper).save(mapValue, value);
        return mapValue;
    }

    private Type instantiableType(String configuredName) throws SerializeException {
        final Type retClass;
        final Class<?> rawType = GenericTypeReflector.erase(this.type);
        if (rawType.isInterface() || Modifier.isAbstract(rawType.getModifiers())) {
            if (configuredName == null) {
                throw new SerializeException(this.type, "No available configured type for instances of this type");
            } else {
                try {
                    retClass = Class.forName(configuredName);
                } catch (final ClassNotFoundException e) {
                    throw new SerializeException(this.type, "Unknown class of object " + configuredName, e);
                }
                if (!GenericTypeReflector.isSuperType(this.type, retClass)) {
                    throw new SerializeException(this.type, "Configured type " + configuredName + " does not extend " + rawType.getCanonicalName());
                }
            }
        } else {
            retClass = this.type;
        }
        return retClass;
    }
}
