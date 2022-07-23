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

import de.natrox.serialize.ParserCollection;
import de.natrox.serialize.demo.util.Mood;
import de.natrox.serialize.exception.SerializeException;
import de.natrox.serialize.parse.Parser;
import io.leangen.geantyref.TypeToken;

import java.util.List;
import java.util.Map;
import java.util.Set;

public final class ParserCollectionDemo {

    public static void main(String[] args) throws SerializeException {
        ParserCollection collection = ParserCollection.defaults();

        // boolean
        Parser<Boolean> booleanParser = collection.get(Boolean.class);
        if (booleanParser != null) {
            boolean bool = booleanParser.parse("true");
        }

        // enum
        Parser<Mood> enumParser = collection.get(Mood.class);
        if (enumParser != null) {
            Mood mood = enumParser.parse("HAPPY");
        }

        // map
        Parser<Map<String, Integer>> mapParser = collection.get(new TypeToken<>() {

        });
        if (mapParser != null) {
            Map<String, Integer> map = mapParser.parse(Map.of("Test", 5L));
        }

        // set
        Parser<Set<String>> setParser = collection.get(new TypeToken<>() {

        });
        if(setParser != null) {
            Set<String> set = setParser.parse(Set.of(1, 2, 3, 10, 100));
        }

        // list
        Parser<List<String>> listParser = collection.get(new TypeToken<>() {

        });
        if(listParser != null) {
            List<String> list = listParser.parse(List.of(1, 2, 3, 10, 100));
        }
    }
}
