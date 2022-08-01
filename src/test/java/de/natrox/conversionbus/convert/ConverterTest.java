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

import de.natrox.conversionbus.exception.SerializeException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConverterTest {

    @Test
    void testBooleanParser() throws SerializeException {
        boolean expected = true;

        // round trip value
        assertEquals(expected, Converters.BOOLEAN.read(expected));

        String[] trueEvaluating = new String[]{"true", "yes", "1", "t", "y"};
        String[] falseEvaluating = new String[]{"false", "no", "0", "f", "n"};

        // from string (true)
        for (final String val : trueEvaluating) {
            assertEquals(true, Converters.BOOLEAN.read(val));
        }

        // from string (false)
        for (final String val : falseEvaluating) {
            assertEquals(false, Converters.BOOLEAN.read(val));
        }
    }

    @Test
    void testFileSerializer() throws SerializeException {
        File expected = new File("hello/world.png");

        // round trip value
        assertEquals(expected, Converters.FILE.read(expected));

        // from string
        assertEquals(expected, Converters.FILE.read("hello/world.png"));
    }

    @Test
    void testCharSerializer() throws SerializeException {
        char expected = 'P';

        // round trip value
        assertEquals(expected, Converters.CHAR.read(expected));

        // from string
        assertEquals('e', Converters.CHAR.read("e"));

        // from byte
        assertEquals('*', Converters.CHAR.read(0x2a));
    }

    @Test
    void testPathSerializer() throws SerializeException {
        Path expected = Path.of("test", "file.txt");

        // round trip value
        assertEquals(expected, Converters.PATH.read(expected));

        // from string
        assertEquals(expected, Converters.PATH.read("test" + FileSystems.getDefault().getSeparator() + "file.txt"));

        // from string list
        assertEquals(expected, Converters.PATH.read(List.of("test", "file.txt")));
    }

    @Test
    void testStringParser() throws SerializeException {
        String expected = "foo";

        // round trip value
        assertEquals(expected, Converters.STRING.read(expected));

        // from integer
        assertEquals("5", Converters.STRING.read(5));

        // from boolean
        assertEquals("true", Converters.STRING.read(true));

        // from double
        assertEquals("0.5", Converters.STRING.read(0.5D));

        // from time unit
        assertEquals("HOURS", Converters.STRING.read(TimeUnit.HOURS));
    }

    @Test
    void testUriSerializer() throws SerializeException, MalformedURLException {
        String uriString = "https://natrox.de";
        URI expected = URI.create(uriString);

        // round trip value
        assertEquals(expected, Converters.URI.read(expected));

        // from string
        assertEquals(expected, Converters.URI.read(uriString));

        // from url
        assertEquals(expected, Converters.URI.read(expected.toURL()));
    }

    @Test
    void testUrlSerializer() throws SerializeException, MalformedURLException, URISyntaxException {
        String urlString = "https://natrox.de";
        URL expected = new URL(urlString);

        // round trip value
        assertEquals(expected, Converters.URL.read(expected));

        // from string
        assertEquals(expected, Converters.URL.read(urlString));

        // from uri
        assertEquals(expected, Converters.URL.read(expected.toURI()));
    }

    @Test
    void testUuidSerializer() throws SerializeException {
        UUID expected = UUID.randomUUID();

        // round trip value
        assertEquals(expected, Converters.UUID.read(expected));

        // from string
        assertEquals(expected, Converters.UUID.read(expected.toString()));
    }
}
