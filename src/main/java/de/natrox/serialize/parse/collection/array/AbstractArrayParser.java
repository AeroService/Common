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

import de.natrox.serialize.ParserCollection;
import de.natrox.serialize.exception.SerializeException;
import de.natrox.serialize.parse.collection.AbstractCollectionParser;
import io.leangen.geantyref.GenericTypeReflector;

import java.lang.reflect.Type;

public abstract class AbstractArrayParser<T> extends AbstractCollectionParser<T> {

    protected AbstractArrayParser(Type type, ParserCollection collection) {
        super(type, collection);
    }

    @Override
    protected Type elementType(final Type containerType) throws SerializeException {
        Type componentType = GenericTypeReflector.getArrayComponentType(containerType);
        if (componentType == null) {
            throw new SerializeException(containerType, "Must be array type");
        }
        return componentType;
    }
}
