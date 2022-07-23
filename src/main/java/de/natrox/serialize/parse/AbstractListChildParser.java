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

package de.natrox.serialize.parse;

import de.natrox.common.consumer.ThrowableConsumer;
import de.natrox.serialize.ParserCollection;
import de.natrox.serialize.exception.SerializeException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.List;

public abstract class AbstractListChildParser<T> implements Parser<T> {

    private final Type type;
    private final ParserCollection collection;

    protected AbstractListChildParser(Type type, ParserCollection collection) {
        this.type = type;
        this.collection = collection;
    }

    @Override
    public @NotNull T parse(@NotNull Object value) throws SerializeException {
        @Nullable Parser<?> entrySerial = this.collection.get(this.type);
        if (entrySerial == null) {
            throw new SerializeException(this.type, "No applicable type serializer for type");
        }

        if (value instanceof List<?> values) {
            final T ret = this.createNew(values.size(), this.type);
            for (int i = 0; i < values.size(); ++i) {
                this.deserializeSingle(i, ret, entrySerial.parse(values.get(i)));
            }
            return ret;
        } else {
            final T ret = this.createNew(1, this.type);
            this.deserializeSingle(0, ret, entrySerial.parse(value));
            return ret;
        }
    }

    protected Type elementType(final Type containerType) throws SerializeException {
        throw new IllegalStateException("AbstractListChildSerializer implementations should override elementType(AnnotatedType)");
    }

    protected T createNew(final int length, final Type elementType) throws SerializeException {
        throw new IllegalStateException("AbstractListChildSerializer implementations should override createNew(int, AnnotatedType)");
    }

    protected abstract void forEachElement(T collection, ThrowableConsumer<Object, SerializeException> action) throws SerializeException;

    protected abstract void deserializeSingle(int index, T collection, @Nullable Object deserialized) throws SerializeException;

}
