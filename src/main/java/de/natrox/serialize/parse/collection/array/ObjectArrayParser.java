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

package de.natrox.serialize.parse.collection.array;

import de.natrox.common.consumer.ThrowableConsumer;
import de.natrox.serialize.ParserCollection;
import de.natrox.serialize.exception.SerializeException;
import io.leangen.geantyref.GenericTypeReflector;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.lang.reflect.Type;

@SuppressWarnings("unchecked")
final class ObjectArrayParser<T> extends AbstractArrayParser<T[]> implements ArrayParser<T[]> {

    ObjectArrayParser(Type type, ParserCollection collection) {
        super(type, collection);
    }

    @Override
    protected T[] createNew(int length, Type elementType) {
        return (T[]) Array.newInstance(GenericTypeReflector.erase(elementType), length);
    }

    @Override
    protected void forEachElement(T[] collection, ThrowableConsumer<Object, SerializeException> action) throws SerializeException {
        for (Object o : collection) {
            action.accept(o);
        }
    }

    @Override
    protected void deserializeSingle(int index, T[] collection, @Nullable Object deserialized) {
        collection[index] = (T) deserialized;
    }
}
