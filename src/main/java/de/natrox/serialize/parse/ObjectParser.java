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

import de.natrox.serialize.objectmapping.ObjectMapper;
import io.leangen.geantyref.TypeToken;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.Map;

public interface ObjectParser<T> extends Parser<T, Map<String, Object>> {

    static <T> @NotNull ObjectParser<T> create() {
        return new ObjectParserImpl<>(new TypeToken<>(){}.getType(), ObjectMapper.factory());
    }

    static <T> @NotNull ObjectParser<T> create(Type type) {
        return new ObjectParserImpl<>(type, ObjectMapper.factory());
    }

}
