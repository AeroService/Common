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

package de.natrox.serialize.demo;

import de.natrox.serialize.demo.util.Mood;
import de.natrox.serialize.demo.util.TestObject;
import de.natrox.serialize.exception.SerializeException;
import de.natrox.serialize.parse.*;
import de.natrox.serialize.parse.collection.ListParser;
import de.natrox.serialize.parse.collection.SetParser;
import io.leangen.geantyref.TypeToken;

import java.util.List;
import java.util.Map;
import java.util.Set;

public final class ParserDemo {

    public static void main(String[] args) throws SerializeException {
        // boolean
        Parser<Boolean> booleanParser = Parsers.BOOLEAN;
        boolean bool = booleanParser.parse("true");

        // enum
        EnumParser<Mood> enumParser = EnumParser.create(Mood.class);
        Mood mood = enumParser.parse("HAPPY");

        // map
        MapParser<String, Integer> mapParser = MapParser.create(new TypeToken<>() {

        });
        Map<String, Integer> map = mapParser.parse(Map.of("Test", 5L));

        // set
        Parser<Set<String>> setParser = SetParser.create(new TypeToken<>() {

        });
        Set<String> set = setParser.parse(Set.of(1, 2, 3, 10, 100));

        // list
        Parser<List<String>> listParser = ListParser.create(new TypeToken<>() {

        });
        List<String> list = listParser.parse(List.of(1, 2, 3, 10, 100));

        // object (object-mapping)
        ObjectParser<TestObject> objectParser = ObjectParser.create(TestObject.class);
        TestObject testObject = objectParser.parse(Map.of("name", "Test", "test", "true"));
    }
}
