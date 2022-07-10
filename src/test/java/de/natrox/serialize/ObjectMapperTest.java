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
import de.natrox.serialize.objectmapping.FieldDiscoverer;
import de.natrox.serialize.objectmapping.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Map;

class ObjectMapperTest {

    @Test
    void test() throws SerializeException {
        Map<String, Object> data = Map.of("name", true, "test", 0, "innerObject", Map.of("innerName", "test"));

        System.out.println(data.toString());

        TestObject testObject = (TestObject) SerializerCollection.defaults().deserialize(data, TestObject.class);

        System.out.println(testObject.name + " : " +  testObject.name.getClass());
        System.out.println(testObject.test);
        System.out.println(testObject.innerObject.innerName);
    }

}
