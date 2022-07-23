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

import de.natrox.serialize.exception.CoercionFailedException;
import de.natrox.serialize.exception.SerializeException;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@SuppressWarnings("unchecked")
final class PathParser implements Parser<Path> {

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    @Override
    public @NotNull Path parse(@NotNull Object obj) throws SerializeException {
        if (obj instanceof List<?> listValue) {
            try {
                List<String> elements = (List<String>) listValue;
                if (elements.isEmpty()) {
                    return Paths.get(".");
                } else if (elements.size() == 1) {
                    return Paths.get(elements.get(0));
                }

                return Paths.get(elements.get(0), elements.subList(1, elements.size()).toArray(EMPTY_STRING_ARRAY));
            } catch (ClassCastException e) {
                throw new CoercionFailedException(e, "Path");
            }
        }

        if (obj instanceof URI) {
            return Paths.get((URI) obj);
        } else {
            return Paths.get(obj.toString());
        }
    }
}
