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

package de.natrox.conversionbus.objectmapping;

import de.natrox.common.function.ThrowableFunction;
import de.natrox.common.validate.Check;
import de.natrox.conversionbus.exception.SerializeException;
import io.leangen.geantyref.GenericTypeReflector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.lang.reflect.Type;
import java.util.*;

final class ObjectMapperFactoryImpl implements ObjectMapper.Factory {

    static final ObjectMapper.Factory INSTANCE = ObjectMapper.factoryBuilder().addDiscoverer(FieldDiscoverer.create()).build();
    private static final int MAXIMUM_MAPPERS_SIZE = 64;

    private final Map<Type, ObjectMapper<?>> mappers = new LinkedHashMap<>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<Type, ObjectMapper<?>> eldest) {
            return size() > MAXIMUM_MAPPERS_SIZE;
        }
    };
    private final List<FieldDiscoverer<?>> fieldDiscoverers;

    ObjectMapperFactoryImpl(BuilderImpl builder) {
        this.fieldDiscoverers = new ArrayList<>(builder.discoverer);
        Collections.reverse(this.fieldDiscoverers);
    }

    @Override
    public @NotNull ObjectMapper<?> get(@NotNull Type type) throws SerializeException {
        Check.notNull(type, "type");
        if (GenericTypeReflector.isMissingTypeParameters(type)) {
            throw new SerializeException(type, "Raw types are not supported!");
        }

        synchronized (this.mappers) {
            return computeFromMap(this.mappers, type, this::computeMapper);
        }
    }

    private ObjectMapper<?> computeMapper(Type type) throws SerializeException {
        for (FieldDiscoverer<?> discoverer : this.fieldDiscoverers) {
            ObjectMapper<?> result = newMapper(type, discoverer);
            if (result != null) {
                return result;
            }
        }

        throw new SerializeException(type, "Could not find factory for type " + type);
    }

    private <T, U> ObjectMapper<T> newMapper(Type type, FieldDiscoverer<U> discoverer) throws SerializeException {
        List<FieldInfo<T, U>> fields = new ArrayList<>();
        FieldDiscoverer.InstanceFactory<U> candidate = discoverer.discover(type, fields);

        if (candidate == null) {
            return null;
        }

        return new ObjectMapperImpl<>(type, fields, candidate);
    }

    private <K, V, E extends Exception> V computeFromMap(Map<K, V> map, K key, ThrowableFunction<K, V, E> creator) {
        return map.computeIfAbsent(key, k -> {
            try {
                return creator.apply(k);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    static class BuilderImpl implements ObjectMapper.Factory.Builder {

        private final List<FieldDiscoverer<?>> discoverer = new ArrayList<>();

        @Override
        public @NotNull Builder addDiscoverer(@NotNull FieldDiscoverer<?> discoverer) {
            this.discoverer.add(discoverer);
            return this;
        }

        @Override
        public ObjectMapper.@UnknownNullability Factory build() {
            return new ObjectMapperFactoryImpl(this);
        }
    }
}
