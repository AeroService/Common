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

import de.natrox.common.builder.IBuilder;
import de.natrox.common.function.SingleTypeFunction;
import de.natrox.common.validate.Check;
import io.leangen.geantyref.TypeToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

public sealed interface SerializerCollection permits SerializerCollectionImpl {

    static @NotNull SerializerCollection.Builder builder() {
        return new SerializerCollectionImpl.BuilderImpl(null);
    }

    static @NotNull SerializerCollection defaults() {
        return SerializerCollectionImpl.DEFAULT;
    }

    default SerializerCollection.Builder childBuilder() {
        return new SerializerCollectionImpl.BuilderImpl(this);
    }

    <T> @Nullable Deserializer<T> get(@NotNull Type type);

    default <T> @Nullable Deserializer<T> get(@NotNull Class<T> type) {
        Check.notNull(type, "type");
        return this.get((Type) type);
    }

    default <T> @Nullable Deserializer<T> get(@NotNull TypeToken<T> typeToken) {
        Check.notNull(typeToken, "typeToken");
        return this.get(typeToken.getType());
    }

    <T, U> @Nullable SpecificDeserializer<T, U> get(@NotNull Type firstType, @NotNull Type secondType);

    default <T, U> @Nullable SpecificDeserializer<T, U> get(@NotNull Class<T> firstType, @NotNull Class<U> secondType) {
        Check.notNull(firstType, "firstType");
        Check.notNull(secondType, "secondType");
        return this.get(firstType, (Type) secondType);
    }

    default <T, U> @Nullable SpecificDeserializer<T, U> get(@NotNull TypeToken<T> firstType, @NotNull TypeToken<U> secondType) {
        Check.notNull(firstType, "firstType");
        Check.notNull(secondType, "secondType");
        return this.get(firstType.getType(), secondType.getType());
    }

    interface Builder extends IBuilder<SerializerCollection> {

        <T, U> SerializerCollection.@NotNull Builder register(@NotNull SpecificDeserializer<T, U> deserializer, @NotNull Registrable<T, U> registrable);

        default <T, U> SerializerCollection.@NotNull Builder register(@NotNull SpecificDeserializer<T, U> deserializer, @NotNull SingleTypeFunction<Registrable<T, U>> function) {
            return this.register(deserializer, function.apply(Registrable.create()));
        }

        default SerializerCollection.@NotNull Builder register(@NotNull TypeDeserializer<?> deserializer) {
            return this.register(deserializer, registrable -> registrable.typeExact(deserializer.type().getType()).inputType(Object.class));
        }
    }
}
