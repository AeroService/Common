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

import de.natrox.serialize.ParserCollection;
import de.natrox.serialize.exception.SerializeException;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

final class MapParserImpl<T, U> implements MapParser<T, U> {

    private final Type type;
    private final ParserCollection collection;

    MapParserImpl(Type type, ParserCollection collection) {
        this.type = type;
        this.collection = collection;
    }

    @Override
    public @NotNull Map<T, U> parse(@NotNull Object obj) throws SerializeException {
        Map<T, U> ret = new LinkedHashMap<>();
        if (obj instanceof Map<?, ?> map) {
            if (!(this.type instanceof final ParameterizedType param)) {
                throw new SerializeException(this.type, "Raw types are not supported for collections");
            }

            Type[] typeArgs = param.getActualTypeArguments();
            if (typeArgs.length != 2) {
                throw new SerializeException(this.type, "Map expected two type arguments!");
            }

            Type key = typeArgs[0];
            Type value = typeArgs[1];
            Parser<T> keyParser = this.collection.get(key);
            Parser<U> valueParser = this.collection.get(value);

            if (keyParser == null) {
                throw new SerializeException(this.type, "No type serializer available for key type " + key);
            }

            if (valueParser == null) {
                throw new SerializeException(this.type, "No type serializer available for value type " + value);
            }

            for (Map.Entry<?, ?> ent : map.entrySet()) {
                ret.put(keyParser.parse(ent.getKey()), valueParser.parse(ent.getValue()));
            }
        }

        return ret;
    }
}
