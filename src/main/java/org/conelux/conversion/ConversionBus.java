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

package org.conelux.conversion;

import org.conelux.common.builder.IBuilder;
import org.conelux.common.validate.Check;
import org.conelux.conversion.converter.Converter;
import io.leangen.geantyref.GenericTypeReflector;
import io.leangen.geantyref.TypeToken;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import org.jetbrains.annotations.NotNull;

public sealed interface ConversionBus permits ConversionBusImpl {

    static @NotNull ConversionBus.Builder builder() {
        return new ConversionBusImpl.BuilderImpl(null);
    }

    static @NotNull ConversionBus defaults() {
        return ConversionBusImpl.DEFAULT;
    }

    default ConversionBus.Builder childBuilder() {
        return new ConversionBusImpl.BuilderImpl(this);
    }

    <T, U> @NotNull Converter<T, U> get(@NotNull Type inputType, @NotNull Type outputType);

    default <T, U> @NotNull Converter<T, U> get(@NotNull Type outputType) {
        Check.notNull(outputType, "outputType");
        return this.get(Object.class, outputType);
    }

    default <T, U> @NotNull Converter<T, U> get(@NotNull Class<T> inputType, @NotNull Class<U> outputType) {
        Check.notNull(inputType, "inputType");
        Check.notNull(outputType, "outputType");
        return this.get(inputType, (Type) outputType);
    }

    default <T, U> @NotNull Converter<T, U> get(@NotNull Class<U> outputType) {
        Check.notNull(outputType, "outputType");
        return this.get((Type) outputType);
    }

    default <T, U> @NotNull Converter<T, U> get(@NotNull TypeToken<T> inputTypeToken,
        @NotNull TypeToken<U> outputTypeToken) {
        Check.notNull(inputTypeToken, "inputTypeToken");
        Check.notNull(outputTypeToken, "outputTypeToken");
        return this.get(inputTypeToken.getType(), outputTypeToken.getType());
    }

    default <T, U> @NotNull Converter<T, U> get(@NotNull TypeToken<U> outputTypeToken) {
        Check.notNull(outputTypeToken, "outputTypeToken");
        return this.get(outputTypeToken.getType());
    }

    interface Builder extends IBuilder<ConversionBus> {

        ConversionBus.@NotNull Builder registerProvider(@NotNull BiPredicate<Type, Type> test,
            @NotNull BiFunction<Type, Type, Converter<?, ?>> function);

        default <T, U> ConversionBus.@NotNull Builder registerProvider(@NotNull Class<? super U> type,
            @NotNull BiFunction<Type, Type, Converter<? super T, ? super U>> parser) {
            Check.notNull(type, "type");
            Check.notNull(parser, "parser");
            return this.registerProvider((input, output) -> this.testType(output, type), parser::apply);
        }

        default <T, U> ConversionBus.@NotNull Builder registerProvider(@NotNull TypeToken<? super U> typeToken,
            @NotNull BiFunction<Type, Type, Converter<? super T, ? super U>> parser) {
            Check.notNull(typeToken, "typeToken");
            Check.notNull(parser, "parser");
            return this.registerProvider((input, output) -> this.testType(output, typeToken.getType()), parser::apply);
        }

        default <T, U> ConversionBus.@NotNull Builder register(@NotNull BiPredicate<Type, Type> test,
            @NotNull Converter<T, U> converter) {
            Check.notNull(test, "test");
            Check.notNull(converter, "parser");
            return this.registerProvider(test, (input, output) -> converter);
        }

        default <T, U> ConversionBus.@NotNull Builder register(@NotNull Class<? super U> type,
            @NotNull Converter<T, U> converter) {
            Check.notNull(type, "type");
            Check.notNull(converter, "parser");
            return this.registerProvider((input, output) -> this.testType(output, type), (input, output) -> converter);
        }

        default <T, U> ConversionBus.@NotNull Builder register(@NotNull Class<? super T> in,
            @NotNull Class<? super U> out, @NotNull Converter<T, U> converter) {
            Check.notNull(in, "type");
            Check.notNull(out, "type");
            Check.notNull(converter, "parser");
            return this.registerProvider((input, output) -> this.testType(output, out) && this.testType(input, in),
                (input, output) -> converter);
        }

        default <T, U> ConversionBus.@NotNull Builder register(@NotNull TypeToken<? super U> typeToken,
            @NotNull Converter<T, U> converter) {
            Check.notNull(typeToken, "typeToken");
            Check.notNull(converter, "parser");
            return this.registerProvider((input, output) -> this.testType(output, typeToken.getType()),
                (input, output) -> converter);
        }

        default <T, U> ConversionBus.@NotNull Builder registerExactProvider(@NotNull Class<U> type,
            @NotNull BiFunction<Type, Type, Converter<T, U>> parser) {
            Check.notNull(type, "type");
            Check.notNull(parser, "parser");
            return this.registerProvider((input, output) -> output.equals(type), parser::apply);
        }

        default <T, U> ConversionBus.@NotNull Builder registerExactProvider(@NotNull TypeToken<U> typeToken,
            @NotNull BiFunction<Type, Type, Converter<T, U>> parser) {
            Check.notNull(typeToken, "typeToken");
            Check.notNull(parser, "parser");
            return this.registerProvider((input, output) -> output.equals(typeToken.getType()), parser::apply);
        }

        default <T, U> ConversionBus.@NotNull Builder registerExact(@NotNull Class<U> type,
            @NotNull Converter<T, U> converter) {
            Check.notNull(type, "type");
            Check.notNull(converter, "parser");
            return this.register((input, output) -> output.equals(type), converter);
        }

        default <T, U> ConversionBus.@NotNull Builder registerExact(@NotNull Class<T> in, @NotNull Class<U> out,
            @NotNull Converter<? super T, ? super U> converter) {
            Check.notNull(in, "type");
            Check.notNull(out, "out");
            Check.notNull(converter, "parser");
            return this.register((input, output) -> output.equals(out) && input.equals(in), converter);
        }

        default <T, U> ConversionBus.@NotNull Builder registerExact(@NotNull TypeToken<U> typeToken,
            @NotNull Converter<T, U> converter) {
            Check.notNull(typeToken, "typeToken");
            Check.notNull(converter, "parser");
            return this.register((input, output) -> output.equals(typeToken.getType()), converter);
        }

        default <T, U> ConversionBus.@NotNull Builder registerExact(@NotNull TypeToken<T> in, @NotNull TypeToken<U> out,
            @NotNull Converter<T, U> converter) {
            Check.notNull(in, "typeToken");
            Check.notNull(out, "typeToken");
            Check.notNull(converter, "parser");
            return this.register((input, output) -> output.equals(out.getType()) && input.equals(in.getType()),
                converter);
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
