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

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;

public final class Converters {

    public final static Converter<Object, Boolean> BOOLEAN = new BooleanConverterImpl();

    public final static Converter<Object, String> STRING = new StringConverterImpl();

    public final static Converter<Object, Character> CHAR = new CharConverterImpl();

    public final static Converter<Object, URI> URI = new UriConverterImpl();

    public final static Converter<Object, URL> URL = new UrlConverterImpl();

    public final static Converter<Object, java.util.UUID> UUID = new UuidConverterImpl();

    public final static Converter<Object, Path> PATH = new PathConverterImpl();

    public final static Converter<Object, File> FILE = new FileConverterImpl();

    public final static Converter<Object, Byte> BYTE = new NumericTypeEnforcerImpl.ByteConverter();

    public final static Converter<Object, Short> SHORT = new NumericTypeEnforcerImpl.ShortConverter();

    public final static Converter<Object, Integer> INTEGER = new NumericTypeEnforcerImpl.IntegerConverter();

    public final static Converter<Object, Long> LONG = new NumericTypeEnforcerImpl.LongConverter();

    public final static Converter<Object, Float> FLOAT = new NumericTypeEnforcerImpl.FloatConverter();

    public final static Converter<Object, Double> DOUBLE = new NumericTypeEnforcerImpl.DoubleConverter();

    private Converters() {
        throw new UnsupportedOperationException();
    }

}
