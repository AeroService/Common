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

package de.natrox.conversionbus.convert;

import de.natrox.conversionbus.exception.ConversionFailedException;
import de.natrox.conversionbus.exception.SerializeException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;

final class FileConverterImpl implements Converter<Object, File> {

    @Override
    public @NotNull File read(@NotNull Object obj) throws SerializeException {
        if (obj instanceof File fileValue) {
            return fileValue;
        }

        if (obj instanceof Path pathValue) {
            return pathValue.toFile();
        }

        if (obj instanceof URI uriValue) {
            return new File(uriValue);
        }

        if (obj instanceof String strValue) {
            return new File(strValue);
        }

        throw new ConversionFailedException(obj, "File");
    }
}
