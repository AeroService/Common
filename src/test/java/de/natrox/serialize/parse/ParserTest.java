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

import de.natrox.serialize.exception.SerializeException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParserTest {

    @Test
    void testBooleanParser() throws SerializeException {
        boolean expected = true;

        // round trip value
        assertEquals(expected, Parsers.BOOLEAN.parse(expected));

        String[] trueEvaluating = new String[]{"true", "yes", "1", "t", "y"};
        String[] falseEvaluating = new String[]{"false", "no", "0", "f", "n"};

        // from string (true)
        for (final String val : trueEvaluating) {
            assertEquals(true, Parsers.BOOLEAN.parse(val));
        }

        // from string (false)
        for (final String val : falseEvaluating) {
            assertEquals(false, Parsers.BOOLEAN.parse(val));
        }
    }

    @Test
    void testFileSerializer() throws SerializeException {
        File expected = new File("hello/world.png");

        // round trip value
        assertEquals(expected, Parsers.FILE.parse(expected));

        // from string
        assertEquals(expected, Parsers.FILE.parse("hello/world.png"));
    }

    @Test
    void testCharSerializer() throws SerializeException {
        char expected = 'P';

        // round trip value
        assertEquals(expected, Parsers.CHAR.parse(expected));

        // from string
        assertEquals('e', Parsers.CHAR.parse("e"));

        // from byte
        assertEquals('*', Parsers.CHAR.parse(0x2a));
    }

    @Test
    void testPathSerializer() throws SerializeException {
        Path expected = Path.of("test", "file.txt");

        // round trip value
        assertEquals(expected, Parsers.PATH.parse(expected));

        // from string
        assertEquals(expected, Parsers.PATH.parse("test" + FileSystems.getDefault().getSeparator() + "file.txt"));

        // from string list
        assertEquals(expected, Parsers.PATH.parse(List.of("test", "file.txt")));
    }

    @Test
    void testStringParser() throws SerializeException {
        String expected = "foo";

        // round trip value
        assertEquals(expected, Parsers.STRING.parse(expected));

        // from integer
        assertEquals("5", Parsers.STRING.parse(5));

        // from boolean
        assertEquals("true", Parsers.STRING.parse(true));

        // from double
        assertEquals("0.5", Parsers.STRING.parse(0.5D));

        // from time unit
        assertEquals("HOURS", Parsers.STRING.parse(TimeUnit.HOURS));
    }

    @Test
    void testUriSerializer() throws SerializeException, MalformedURLException {
        String uriString = "https://natrox.de";
        URI expected = URI.create(uriString);

        // round trip value
        assertEquals(expected, Parsers.URI.parse(expected));

        // from string
        assertEquals(expected, Parsers.URI.parse(uriString));

        // from url
        assertEquals(expected, Parsers.URI.parse(expected.toURL()));
    }

    @Test
    void testUrlSerializer() throws SerializeException, MalformedURLException, URISyntaxException {
        String urlString = "https://natrox.de";
        URL expected = new URL(urlString);

        // round trip value
        assertEquals(expected, Parsers.URL.parse(expected));

        // from string
        assertEquals(expected, Parsers.URL.parse(urlString));

        // from uri
        assertEquals(expected, Parsers.URL.parse(expected.toURI()));
    }

    @Test
    void testUuidSerializer() throws SerializeException {
        UUID expected = UUID.randomUUID();

        // round trip value
        assertEquals(expected, Parsers.UUID.parse(expected));

        // from string
        assertEquals(expected, Parsers.UUID.parse(expected.toString()));
    }
}
