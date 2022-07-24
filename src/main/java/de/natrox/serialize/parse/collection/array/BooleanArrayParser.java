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
import de.natrox.serialize.parse.Parsers;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

final class BooleanArrayParser extends AbstractArrayParser<boolean[]> implements ArrayParser<boolean[]> {

    BooleanArrayParser(ParserCollection collection) {
        super(boolean[].class, collection);
    }

    @Override
    protected boolean[] createNew(int length, Type elementType) throws SerializeException {
        return new boolean[length];
    }

    @Override
    protected void forEachElement(boolean[] collection, ThrowableConsumer<Object, SerializeException> action) throws SerializeException {
        for (boolean b : collection) {
            action.accept(b);
        }
    }

    @Override
    protected void deserializeSingle(int index, boolean[] collection, @Nullable Object deserialized) throws SerializeException {
        collection[index] = deserialized != null && Parsers.BOOLEAN.parse(deserialized);
    }
}
