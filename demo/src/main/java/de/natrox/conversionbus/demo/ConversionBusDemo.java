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

package de.natrox.conversionbus.demo;

import de.natrox.conversionbus.ConversionBus;
import de.natrox.conversionbus.demo.util.Mood;
import de.natrox.conversionbus.exception.SerializeException;
import de.natrox.conversionbus.convert.Converter;
import io.leangen.geantyref.TypeToken;

import java.util.List;
import java.util.Map;
import java.util.Set;

public final class ConversionBusDemo {

    public static void main(String[] args) throws SerializeException {
        ConversionBus collection = ConversionBus.defaults();

        // boolean
        Converter<Object, Boolean> booleanConverter = collection.get(Boolean.class);
        boolean bool = booleanConverter.read("true");

        // enum
        Converter<Object, Mood> enumConverter = collection.get(Mood.class);
        Mood mood = enumConverter.read("HAPPY");

        // map
        Converter<Object, Map<String, Integer>> mapConverter = collection.get(new TypeToken<>() {

        });
        Map<String, Integer> map = mapConverter.read(Map.of("Test", 5L));

        // set
        Converter<Object, Set<String>> setConverter = collection.get(new TypeToken<>() {

        });
        Set<String> set = setConverter.read(Set.of(1, 2, 3, 10, 100));

        // list
        Converter<Object, List<String>> listConverter = collection.get(new TypeToken<>() {

        });
        List<String> list = listConverter.read(List.of(1, 2, 3, 10, 100));
    }
}
