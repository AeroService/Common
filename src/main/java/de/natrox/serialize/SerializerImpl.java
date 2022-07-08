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

import de.natrox.common.function.ThrowableBiFunction;
import de.natrox.serialize.exception.SerializerNotFoundException;
import de.natrox.serialize.preferences.SerializerPreferences;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class SerializerImpl implements Serializer {

    private final Map<Class<?>, ThrowableBiFunction<?, SerializerPreferences, Object, RuntimeException>> serializer;
    private final Map<Class<?>, ThrowableBiFunction<Object, SerializerPreferences, ?, RuntimeException>> deserializer;
    private final SerializerPreferences preferences;

    SerializerImpl(@NotNull SerializerPreferences preferences) {
        serializer = new HashMap<>();
        deserializer = new HashMap<>();
        this.preferences = preferences;
    }

    @Override
    public SerializerImpl addDefaultOperations() {
        this

                .addSerializer(Boolean.class, BooleanSerializer::serialize)
                .addDeserializer(Boolean.class, BooleanSerializer::deserialize)

                .addSerializer(Character.class, CharacterSerializer::serialize)
                .addDeserializer(Character.class, CharacterSerializer::deserialize);

        return this;
    }

    @Override
    public <T> Object serialize(T value) {
        return this.findSerializer((Class<T>) value.getClass()).apply(value, this.preferences);
    }

    @Override
    public <T> T deserialize(Object value, Class<T> valueClass) {
        if (valueClass.isAssignableFrom(value.getClass()))
            return (T) value;
        return this.findDeserializer(valueClass).apply(value, this.preferences);
    }

    @Override
    public <T> SerializerImpl addSerializer(Class<? extends T> type, ThrowableBiFunction<T, SerializerPreferences, Object, RuntimeException> operation) {
        this.serializer.put(type, operation);
        return this;
    }

    @Override
    public <T> SerializerImpl addDeserializer(Class<? extends T> type, ThrowableBiFunction<Object, SerializerPreferences, T, RuntimeException> operation) {
        this.deserializer.put(type, operation);
        return this;
    }

    private <T> ThrowableBiFunction<T, SerializerPreferences, Object, RuntimeException> findSerializer(Class<T> type) {
        if (this.serializer.containsKey(type))
            return (ThrowableBiFunction<T, SerializerPreferences, Object, RuntimeException>) serializer.get(type);

        for (Map.Entry<Class<?>, ThrowableBiFunction<?, SerializerPreferences, Object, RuntimeException>> entry : serializer.entrySet())
            if (entry.getKey().isAssignableFrom(type))
                return (ThrowableBiFunction<T, SerializerPreferences, Object, RuntimeException>) entry.getValue();
        throw new SerializerNotFoundException("No Serializer was found for given type.");
    }

    private <T> ThrowableBiFunction<Object, SerializerPreferences, T, RuntimeException> findDeserializer(Class<T> type) {
        if (this.serializer.containsKey(type))
            return (ThrowableBiFunction<Object, SerializerPreferences, T, RuntimeException>) deserializer.get(type);

        for (Map.Entry<Class<?>, ThrowableBiFunction<Object, SerializerPreferences, ?, RuntimeException>> entry : deserializer.entrySet())
            if (entry.getKey().isAssignableFrom(type))
                return (ThrowableBiFunction<Object, SerializerPreferences, T, RuntimeException>) entry.getValue();
        throw new SerializerNotFoundException("No Serializer was found for given type.");
    }
}
