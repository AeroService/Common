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
import de.natrox.common.validate.Check;
import de.natrox.serialize.exception.SerializeException;
import io.leangen.geantyref.GenericTypeReflector;
import io.leangen.geantyref.TypeToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.function.Predicate;
import java.util.stream.Stream;

public sealed interface SerializerCollection extends ChildSerializer permits SerializerCollectionImpl {

    static @NotNull SerializerCollection.Builder builder() {
        return new SerializerCollectionImpl.BuilderImpl(null);
    }

    static @NotNull SerializerCollection defaults() {
        return SerializerCollectionImpl.DEFAULT;
    }

    default SerializerCollection.Builder childBuilder() {
        return new SerializerCollectionImpl.BuilderImpl(this);
    }

    <T> @Nullable Serializer<T> get(@NotNull Type type);

    default <T> @Nullable Serializer<T> get(@NotNull Class<T> type) {
        Check.notNull(type, "type");
        return this.get((Type) type);
    }

    default <T> @Nullable Serializer<T> get(@NotNull TypeToken<T> typeToken) {
        Check.notNull(typeToken, "typeToken");
        return this.get(typeToken.getType());
    }

    <T> @NotNull T deserialize(@NotNull Object obj, Type @NotNull ... types) throws SerializeException;

    default <T> @NotNull T deserialize(@NotNull Object obj, Class<? super T> @NotNull ... types) throws SerializeException {
        Check.notNull(obj, "object");
        Check.notNull(types, "types");
        Check.argCondition(types.length <= 0, "types");
        return this.deserialize(obj, (Type[]) types);
    }

    default <T> @NotNull T deserialize(@NotNull Object obj, TypeToken<? super T> @NotNull ... typeTokens) throws SerializeException {
        Check.notNull(obj, "object");
        Check.notNull(typeTokens, "types");
        Check.argCondition(typeTokens.length <= 0, "types");
        return this.deserialize(obj, Stream.of(typeTokens).map(TypeToken::getType).toArray(Type[]::new));
    }

    interface Builder extends IBuilder<SerializerCollection> {

        SerializerCollection.@NotNull Builder register(@NotNull ChildSerializer serializer);

        SerializerCollection.@NotNull Builder register(@NotNull Predicate<Type> test, @NotNull Serializer<?> serializer);

        default SerializerCollection.@NotNull Builder register(@NotNull Type type, @NotNull Serializer<?> serializer) {
            Check.notNull(type, "type");
            Check.notNull(serializer, "serializer");
            return this.register(test -> {
                if (GenericTypeReflector.isSuperType(type, test)) {
                    return true;
                }

                if (test instanceof WildcardType) {
                    Type[] upperBounds = ((WildcardType) test).getUpperBounds();
                    if (upperBounds.length == 1) {
                        return GenericTypeReflector.isSuperType(type, upperBounds[0]);
                    }
                }
                return false;
            }, serializer);
        }

        default <T> SerializerCollection.@NotNull Builder register(@NotNull Class<T> type, @NotNull Serializer<? super T> serializer) {
            Check.notNull(type, "type");
            Check.notNull(serializer, "serializer");
            return this.register((Type) type, serializer);
        }

        default <T> SerializerCollection.@NotNull Builder register(@NotNull TypeToken<T> typeToken, @NotNull Serializer<? super T> serializer) {
            Check.notNull(typeToken, "typeToken");
            Check.notNull(serializer, "serializer");
            return this.register(typeToken.getType(), serializer);
        }

        default SerializerCollection.@NotNull Builder register(@NotNull TypeSerializer<?> serializer) {
            Check.notNull(serializer, "serializer");
            return this.register(serializer.type().getType(), serializer);
        }

        default SerializerCollection.@NotNull Builder registerExact(@NotNull Type type, @NotNull Serializer<?> serializer) {
            return this.register(test -> test.equals(type), serializer);
        }

        default <T> SerializerCollection.@NotNull Builder registerExact(@NotNull Class<T> type, @NotNull Serializer<? super T> serializer) {
            Check.notNull(type, "type");
            Check.notNull(serializer, "serializer");
            return this.registerExact((Type) type, serializer);
        }

        default <T> SerializerCollection.@NotNull Builder registerExact(@NotNull TypeToken<T> typeToken, @NotNull Serializer<? super T> serializer) {
            Check.notNull(typeToken, "typeToken");
            Check.notNull(serializer, "serializer");
            return this.registerExact(typeToken.getType(), serializer);
        }

        default SerializerCollection.@NotNull Builder registerExact(@NotNull TypeSerializer<?> serializer) {
            Check.notNull(serializer, "serializer");
            return this.registerExact(serializer.type().getType(), serializer);
        }
    }
}
