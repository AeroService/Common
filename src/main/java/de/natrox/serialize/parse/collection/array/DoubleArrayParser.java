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

final class DoubleArrayParser extends AbstractArrayParser<double[]> implements ArrayParser<double[]> {

    DoubleArrayParser(ParserCollection collection) {
        super(double[].class, collection);
    }

    @Override
    protected double[] createNew(int length, Type elementType) {
        return new double[length];
    }

    @Override
    protected void forEachElement(double[] collection, ThrowableConsumer<Object, SerializeException> action) throws SerializeException {
        for (double b : collection) {
            action.accept(b);
        }
    }

    @Override
    protected void deserializeSingle(int index, double[] collection, @Nullable Object deserialized) throws SerializeException {
        collection[index] = deserialized == null ? 0 : Parsers.DOUBLE.parse(deserialized);
    }
}
