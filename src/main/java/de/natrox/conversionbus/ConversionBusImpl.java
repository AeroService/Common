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

package de.natrox.conversionbus;

import de.natrox.common.container.Pair;
import de.natrox.common.validate.Check;
import de.natrox.conversionbus.exception.ConverterNotFoundException;
import de.natrox.conversionbus.convert.*;
import de.natrox.conversionbus.convert.ListConverter;
import de.natrox.conversionbus.convert.SetConverter;
import io.leangen.geantyref.GenericTypeReflector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

@SuppressWarnings({"unchecked"})
final class ConversionBusImpl implements ConversionBus {

    final static ConversionBus DEFAULT;

    static {
        DEFAULT = ConversionBus
            .builder()
            .registerExact(Boolean.class, Converters.BOOLEAN)
            .registerExact(boolean.class, Converters.BOOLEAN)
            .registerExact(Character.class, Converters.CHAR)
            .registerExact(char.class, Converters.CHAR)
            .registerExact(String.class, Converters.STRING)
            .registerExact(URI.class, Converters.URI)
            .registerExact(URL.class, Converters.URL)
            .registerExact(UUID.class, Converters.UUID)
            .registerExact(Path.class, Converters.PATH)
            .registerExact(File.class, Converters.FILE)
            .registerExact(Byte.class, Converters.BYTE)
            .registerExact(byte.class, Converters.BYTE)
            .registerExact(Short.class, Converters.SHORT)
            .registerExact(short.class, Converters.SHORT)
            .registerExact(Integer.class, Converters.INTEGER)
            .registerExact(int.class, Converters.INTEGER)
            .registerExact(Long.class, Converters.LONG)
            .registerExact(long.class, Converters.LONG)
            .registerExact(Float.class, Converters.FLOAT)
            .registerExact(float.class, Converters.FLOAT)
            .registerExact(Double.class, Converters.DOUBLE)
            .registerExact(double.class, Converters.DOUBLE)
            .registerProvider(Enum.class, (input, output) -> EnumConverter.create(output))
            .registerProvider(Map.class, (input, output) -> MapConverter.create(output))
            .registerProvider(Set.class, (input, output) -> SetConverter.create(output))
            .registerProvider(List.class, (input, output) -> ListConverter.create(output))
            .registerProvider((input, output) -> {
                if(input instanceof ParameterizedType parameterizedType) {
                    return parameterizedType.getActualTypeArguments()[0].equals(String.class);
                }

                return false;
            }, (input, output) -> ObjectConverter.create(output))
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

        throw new IllegalArgumentException("Failed to find converter which converts the input value of type " + inputType.getTypeName() + " to a value of type " + outputType.getTypeName());
    }

    final static class BuilderImpl implements ConversionBus.Builder {

        private final @Nullable ConversionBus parent;
        private final List<RegisteredSerializer> serializers = new ArrayList<>();

        BuilderImpl(@Nullable ConversionBus parent) {
            this.parent = parent;
        }

        @Override
        public @NotNull Builder registerProvider(@NotNull BiPredicate<Type, Type> test, @NotNull BiFunction<Type, Type, Converter<?, ?>> supplier) {
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
