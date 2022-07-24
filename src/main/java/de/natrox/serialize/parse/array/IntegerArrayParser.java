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

package de.natrox.serialize.parse.array;

import de.natrox.common.consumer.ThrowableConsumer;
import de.natrox.serialize.ParserCollection;
import de.natrox.serialize.exception.SerializeException;
import de.natrox.serialize.parse.Parsers;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

final class IntegerArrayParser extends AbstractArrayParser<int[]> implements ArrayParser<int[]> {

    IntegerArrayParser(ParserCollection collection) {
        super(int[].class, collection);
    }

    @Override
    protected int[] createNew(int length, Type elementType) {
        return new int[length];
    }

    @Override
    protected void forEachElement(int[] collection, ThrowableConsumer<Object, SerializeException> action) throws SerializeException {
        for (int b : collection) {
            action.accept(b);
        }
    }

    @Override
    protected void deserializeSingle(int index, int[] collection, @Nullable Object deserialized) throws SerializeException {
        collection[index] = deserialized == null ? 0 : Parsers.INTEGER.parse(deserialized);
    }
}
