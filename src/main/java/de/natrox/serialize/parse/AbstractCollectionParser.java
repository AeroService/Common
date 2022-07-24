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
import de.natrox.serialize.parse.Parser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;

public abstract class AbstractCollectionParser<T> implements Parser<T> {

    private final Type type;
    private final ParserCollection collection;

    protected AbstractCollectionParser(Type type, ParserCollection collection) {
        this.type = type;
        this.collection = collection;
    }

    @Override
    public @NotNull T parse(@NotNull Object value) throws SerializeException {
        Type entryType = this.elementType(this.type);
        @Nullable Parser<?> entrySerial = this.collection.get(entryType);
        if (entrySerial == null) {
            throw new SerializeException(entryType, "No applicable type serializer for type");
        }

        if (value instanceof Collection<?> values) {
            final T ret = this.createNew(values.size(), entryType);
            Iterator<?> iterator = values.iterator();
            for (int i = 0; iterator.hasNext(); i++) {
                this.deserializeSingle(i, ret, entrySerial.parse(iterator.next()));
            }
            return ret;
        } else {
            final T ret = this.createNew(1, entryType);
            this.deserializeSingle(0, ret, entrySerial.parse(value));
            return ret;
        }
    }

    protected abstract Type elementType(final Type containerType) throws SerializeException;

    protected abstract T createNew(final int length, final Type elementType) throws SerializeException;

    protected abstract void forEachElement(T collection, ThrowableConsumer<Object, SerializeException> action) throws SerializeException;

    protected abstract void deserializeSingle(int index, T collection, @Nullable Object deserialized) throws SerializeException;

}
