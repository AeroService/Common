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

import de.natrox.common.validate.Check;
import de.natrox.serialize.SerializerCollection;
import io.leangen.geantyref.TypeToken;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.Map;

public interface MapParser<T, U> extends Parser<Map<T, U>, Object> {

    static <T, U> @NotNull MapParser<T, U> create() {
        return new MapParserImpl<>(new TypeToken<>() {

        }.getType(), SerializerCollection.defaults());
    }

    static <T, U> @NotNull MapParser<T, U> create(@NotNull Type type) {
        Check.notNull(type, "type");
        return new MapParserImpl<>(type, SerializerCollection.defaults());
    }

    static <T, U> @NotNull MapParser<T, U> create(@NotNull SerializerCollection collection) {
        Check.notNull(collection, "collection");
        return new MapParserImpl<>(new TypeToken<>() {

        }.getType(), collection);
    }

    static <T, U> @NotNull MapParser<T, U> create(@NotNull Type type, @NotNull SerializerCollection collection) {
        Check.notNull(type, "type");
        Check.notNull(collection, "collection");
        return new MapParserImpl<>(type, collection);
    }
}
