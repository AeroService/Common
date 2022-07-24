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

import de.natrox.common.function.ThrowableFunction;
import de.natrox.common.validate.Check;
import de.natrox.serialize.ParserCollection;
import de.natrox.serialize.exception.SerializeException;
import de.natrox.serialize.parse.Parser;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unchecked", "ClassCanBeRecord"})
final class ObjectMapperImpl<T, U> implements ObjectMapper<T> {

    private final Type type;
    private final List<FieldInfo<T, U>> fields;
    private final FieldDiscoverer.InstanceFactory<U> instanceFactory;

    ObjectMapperImpl(Type type, List<FieldInfo<T, U>> fields, FieldDiscoverer.InstanceFactory<U> instanceFactory) {
        this.type = type;
        this.fields = Collections.unmodifiableList(fields);
        this.instanceFactory = instanceFactory;
    }

    @Override
    public @NotNull T load(@NotNull Map<String, Object> source) throws SerializeException {
        Check.notNull(source, "source");
        return this.load(source, intermediate -> (T) this.instanceFactory.complete(intermediate));
    }

    @Override
    public void load(@NotNull T value, @NotNull Map<String, Object> source) throws SerializeException {
        Check.notNull(value, "value");
        Check.notNull(source, "source");
        this.load(source, intermediate -> {
            this.instanceFactory.complete(value, intermediate);
            return value;
        });
    }

    private T load(Map<String, Object> source, ThrowableFunction<U, T, SerializeException> completer) throws SerializeException {
        U intermediate = this.instanceFactory.begin();

        for (FieldInfo<T, U> field : this.fields) {
            Parser<?> parser = ParserCollection.defaults().get(field.type());
            if (parser == null) {
                throw new SerializeException("No TypeSerializer found for field " + field.name() + " of type " + field.type());
            }

            Object value = source.get(field.name());

            if (value == null) {
                continue;
            }

            Object newValue = parser.parse(value);
            field.validateValue(newValue);

            field.deserializer().accept(intermediate, newValue);
        }

        return completer.apply(intermediate);
    }

    @Override
    public @NotNull Map<String, Object> save(@NotNull T value) throws SerializeException {
        Check.notNull(value, "value");
        Map<String, Object> target = new HashMap<>();

        this.save(target, value);

        return target;
    }

    @Override
    public void save(@NotNull Map<String, Object> target, @NotNull T value) throws SerializeException {
        Check.notNull(target, "target");
        Check.notNull(value, "value");
        for (FieldInfo<T, U> field : this.fields) {
            try {
                Object fieldValue = field.serializer().apply(value);

                if (fieldValue == null) {
                    target.put(field.name(), null);
                    continue;
                }

                Parser<Object> parser = ParserCollection.defaults().get(field.type());
                if (parser == null) {
                    throw new SerializeException("No TypeSerializer found for field " + field.name() + " of type " + field.type());
                }

                target.put(field.name(), parser.serialize(fieldValue));
            } catch (IllegalAccessException exception) {
                throw new SerializeException(field.type(), exception);
            }
        }
    }
}
