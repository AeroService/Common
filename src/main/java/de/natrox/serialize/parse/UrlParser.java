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

import java.net.MalformedURLException;
import java.net.URL;

final class UrlParser implements Parser<URL> {

    @Override
    public @NotNull URL parse(@NotNull Object obj) throws SerializeException {
        if (obj instanceof String strValue) {
            try {
                return new URL(strValue);
            } catch (MalformedURLException ignored) {

            }
        }

        throw new CoercionFailedException(obj, "URL");
    }
}
