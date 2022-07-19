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
import de.natrox.serialize.parse.Parser;
import io.leangen.geantyref.GenericTypeReflector;
import io.leangen.geantyref.TypeToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.function.Function;
import java.util.function.Predicate;

public sealed interface ParserCollection permits ParserCollectionImpl {

    static @NotNull ParserCollection.Builder builder() {
        return new ParserCollectionImpl.BuilderImpl(null);
    }

    static @NotNull ParserCollection defaults() {
        return ParserCollectionImpl.DEFAULT;
    }

    default ParserCollection.Builder childBuilder() {
        return new ParserCollectionImpl.BuilderImpl(this);
    }

    <T> @Nullable Parser<T> get(@NotNull Type type);

    default <T> @Nullable Parser<T> get(@NotNull Class<T> type) {
        Check.notNull(type, "type");
        return this.get((Type) type);
    }

    default <T> @Nullable Parser<T> get(@NotNull TypeToken<T> typeToken) {
        Check.notNull(typeToken, "typeToken");
        return this.get(typeToken.getType());
    }

    interface Builder extends IBuilder<ParserCollection> {

        ParserCollection.@NotNull Builder register(@NotNull Predicate<Type> test, @NotNull Function<Type, Parser<?>> supplier);

        default <T> ParserCollection.@NotNull Builder register(@NotNull Class<T> type, @NotNull Function<Type, Parser<T>> parser) {
            Check.notNull(type, "type");
            Check.notNull(parser, "parser");
            return this.register(test -> this.testType(test, type), parser::apply);
        }

        default <T> ParserCollection.@NotNull Builder register(@NotNull TypeToken<T> typeToken, @NotNull Function<Type, Parser<T>> parser) {
            Check.notNull(typeToken, "typeToken");
            Check.notNull(parser, "parser");
            return this.register(test -> this.testType(test, typeToken.getType()), parser::apply);
        }

        default <T> ParserCollection.@NotNull Builder registerExact(@NotNull Class<T> type, @NotNull Function<Type, Parser<T>> parser) {
            Check.notNull(type, "type");
            Check.notNull(parser, "parser");
            return this.register(test -> test.equals(type), parser::apply);
        }

        default <T> ParserCollection.@NotNull Builder registerExact(@NotNull TypeToken<T> typeToken, @NotNull Function<Type, Parser<T>> parser) {
            Check.notNull(typeToken, "typeToken");
            Check.notNull(parser, "parser");
            return this.register(test -> test.equals(typeToken.getType()), parser::apply);
        }

        private boolean testType(Type test, Type type) {
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
        }
    }
}
