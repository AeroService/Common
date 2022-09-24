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

package de.natrox.conversion;

import de.natrox.common.container.Pair;
import de.natrox.common.validate.Check;
import de.natrox.conversion.converter.Converter;
import de.natrox.conversion.converter.EnumToStringConverter;
import de.natrox.conversion.converter.ObjectToBooleanConverter;
import de.natrox.conversion.converter.ObjectToStringConverter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

@SuppressWarnings({"unchecked"})
final class ConversionBusImpl implements ConversionBus {

    final static ConversionBus DEFAULT;

    static {
        DEFAULT = ConversionBus
            .builder()
            .register(Object.class, String.class, new ObjectToStringConverter())
            .registerExact(EnumToStringConverter.INPUT_TYPE, EnumToStringConverter.OUTPUT_TYPE,
                new EnumToStringConverter())
            .registerExact(String.class, Boolean.class, new ObjectToBooleanConverter())
            .registerExact(Integer.class, Boolean.class, new ObjectToBooleanConverter())
            .build();
    }

    private final @Nullable ConversionBus parent;
    private final Map<Pair<Type, Type>, BiFunction<Type, Type, Converter<?, ?>>> typeMatches = new ConcurrentHashMap<>();
    List<RegisteredSerializer> serializers;

    ConversionBusImpl(@Nullable ConversionBus parent, List<RegisteredSerializer> serializers) {
        this.parent = parent;
        this.serializers = Collections.unmodifiableList(serializers);
    }

    @Override
    public @NotNull <T, U> Converter<T, U> get(@NotNull Type inputType, @NotNull Type outputType) {
        Check.notNull(inputType, "inputType");
        Check.notNull(outputType, "outputType");
        Pair<Type, Type> key = Pair.of(inputType, outputType);
        BiFunction<Type, Type, Converter<?, ?>> supplier = this.typeMatches.computeIfAbsent(key, param -> {
            for (RegisteredSerializer ent : this.serializers) {
                if (ent.matches(param.first(), param.second())) {
                    return ent.converter();
                }
            }

            return null;
        });

        if (supplier != null) {
            return (Converter<T, U>) supplier.apply(inputType, outputType);
        }

        if (this.parent != null) {
            return this.parent.get(inputType, outputType);
        }

        throw new IllegalArgumentException(
            "Failed to find converter which converts the input value of type " + inputType.getTypeName()
                + " to a value of type " + outputType.getTypeName());
    }

    final static class BuilderImpl implements ConversionBus.Builder {

        private final @Nullable ConversionBus parent;
        private final List<RegisteredSerializer> serializers = new ArrayList<>();

        BuilderImpl(@Nullable ConversionBus parent) {
            this.parent = parent;
        }

        @Override
        public @NotNull Builder registerProvider(@NotNull BiPredicate<Type, Type> test,
            @NotNull BiFunction<Type, Type, Converter<?, ?>> supplier) {
            Check.notNull(test, "test");
            Check.notNull(supplier, "supplier");
            this.serializers.add(new RegisteredSerializer(test, supplier));
            return this;
        }

        @Override
        public @UnknownNullability ConversionBus build() {
            return new ConversionBusImpl(this.parent, this.serializers);
        }
    }

    @SuppressWarnings("ClassCanBeRecord")
    static final class RegisteredSerializer {

        private final BiPredicate<Type, Type> predicate;
        private final BiFunction<Type, Type, Converter<?, ?>> converter;

        RegisteredSerializer(BiPredicate<Type, Type> predicate, BiFunction<Type, Type, Converter<?, ?>> converter) {
            this.predicate = predicate;
            this.converter = converter;
        }

        public boolean matches(Type input, Type output) {
            return this.predicate.test(input, output);
        }

        public BiFunction<Type, Type, Converter<?, ?>> converter() {
            return this.converter;
        }
    }
}
