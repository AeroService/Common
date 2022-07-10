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

package de.natrox.serialize.objectmapping;

import de.natrox.common.builder.IBuilder;
import de.natrox.serialize.Serializer;
import de.natrox.serialize.exception.SerializeException;
import io.leangen.geantyref.TypeToken;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.Map;

public interface ObjectMapper<T> {

    static @NotNull Factory factory() {
        return ObjectMapperFactoryImpl.INSTANCE;
    }

    static @NotNull Factory.Builder factoryBuilder() {
        return new ObjectMapperFactoryImpl.BuilderImpl();
    }

    @NotNull T load(@NotNull Map<String, Object> source) throws SerializeException;

    void load(@NotNull T value, @NotNull Map<String, Object> source) throws SerializeException;

    @NotNull Map<String, Object> save(@NotNull T value) throws SerializeException;

    void save(@NotNull Map<String, Object> target, @NotNull T value) throws Exception;

    interface Factory extends Serializer<Object> {

        @SuppressWarnings("unchecked")
        default <V> @NotNull ObjectMapper<V> get(@NotNull TypeToken<V> type) throws SerializeException {
            return (ObjectMapper<V>) get(type.getType());
        }

        @SuppressWarnings("unchecked")
        default <V> @NotNull ObjectMapper<V> get(@NotNull Class<V> clazz) throws SerializeException {
            return (ObjectMapper<V>) get((Type) clazz);
        }

        @NotNull ObjectMapper<?> get(@NotNull Type type) throws SerializeException;

        interface Builder extends IBuilder<Factory> {

            @NotNull Builder addDiscoverer(@NotNull FieldDiscoverer<?> discoverer);

        }
    }
}
