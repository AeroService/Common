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

package de.natrox.serialize;

import de.natrox.serialize.exception.SerializeException;
import de.natrox.serialize.parse.*;
import io.leangen.geantyref.TypeToken;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ExampleTest {

    @Test
    void parserTest() throws SerializeException {
        Parser<Boolean> booleanParser = Parsers.BOOLEAN;
        boolean bool = booleanParser.parse("true");
        assertTrue(bool);

        EnumParser<Mood> enumParser = EnumParser.create(Mood.class);
        Mood mood = enumParser.parse("HAPPY");
        assertEquals(Mood.HAPPY, mood);

        MapParser<String, Integer> mapParser = MapParser.create(new TypeToken<>() {

        });
        Map<String, Integer> map = mapParser.parse(Map.of("Test", 5L));
        assertEquals(Map.of("Test", 5), map);

        ObjectParser<TestObject> objectParser = ObjectParser.create(TestObject.class);
        TestObject testObject = objectParser.parse(Map.of("name", "Test", "test", "true"));
    }

    @Test
    void collectionTest() throws SerializeException {
        ParserCollection collection = ParserCollection.defaults();

        Parser<Boolean> booleanParser = collection.get(Boolean.class);
        assertNotNull(booleanParser);

        boolean bool = booleanParser.parse("true");
        assertTrue(bool);

        Parser<Mood> enumParser = collection.get(Mood.class);
        assertNotNull(enumParser);

        Mood mood = enumParser.parse("HAPPY");
        assertEquals(Mood.HAPPY, mood);

        Parser<Map<String, Integer>> mapParser = collection.get(new TypeToken<>() {});
        assertNotNull(mapParser);

        Parser<Set<String>> setParser = collection.get(new TypeToken<>() {});
        assertNotNull(setParser);

        Parser<List<String>> listParser = collection.get(new TypeToken<>() {});
        assertNotNull(listParser);

        Map<String, Integer> map = mapParser.parse(Map.of("Test", 5L));
        assertEquals(Map.of("Test", 5), map);
    }

}
