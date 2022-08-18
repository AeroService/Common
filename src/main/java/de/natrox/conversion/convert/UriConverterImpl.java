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

package de.natrox.conversion.convert;

import de.natrox.conversion.exception.ConversionFailedException;
import de.natrox.conversion.exception.SerializeException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

final class UriConverterImpl implements Converter<Object, URI> {

    @Override
    public @NotNull URI read(@NotNull Object obj) throws SerializeException {
        try {
            if (obj instanceof URI uriValue) {
                return uriValue;
            }

            if (obj instanceof URL urlValue) {
                return urlValue.toURI();
            }

            if(obj instanceof File fileValue) {
                return fileValue.toURI();
            }

            if (obj instanceof String strValue) {
                return new URI(strValue);
            }
        } catch (URISyntaxException ignored) {

        }

        throw new ConversionFailedException(obj, "URI");
    }
}
