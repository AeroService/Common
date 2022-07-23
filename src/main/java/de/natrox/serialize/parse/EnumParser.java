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

import io.leangen.geantyref.TypeToken;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public interface EnumParser<T extends Enum<T>> extends Parser<T> {

    static <T extends Enum<T>> @NotNull EnumParser<T> create(Type type) {
        return new EnumParserImpl<>(type);
    }

    static <T extends Enum<T>> @NotNull EnumParser<T> create(Class<T> type) {
        return new EnumParserImpl<>(type);
    }

    static <T extends Enum<T>> @NotNull EnumParser<T> create(TypeToken<T> typeToken) {
        return new EnumParserImpl<>(typeToken.getType());
    }
}
