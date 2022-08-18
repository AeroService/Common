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

package de.natrox.conversion.objectmapping;

import de.natrox.conversion.exception.SerializeException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("unchecked")
class ObjectMapperTest {

    @Test
    void testLoad() throws SerializeException {
        ObjectMapper<TestObject> mapper = ObjectMapper.factory().get(TestObject.class);
        Map<String, Object> source = Map.of("test", "foo");

        TestObject obj = mapper.load(source);
        assertEquals("foo", obj.test);
    }

    @Test
    void testLoadExistingObject() throws SerializeException {
        ObjectMapper<TestObject> mapper = ObjectMapper.factory().get(TestObject.class);
        Map<String, Object> source = Map.of("test", "foo");

        TestObject obj = new TestObject();
        mapper.load(obj, source);
        assertEquals("foo", obj.test);
    }

    @Test
    void testLoadNullsPreserved() throws SerializeException {
        ObjectMapper<TestObject> mapper = ObjectMapper.factory().get(TestObject.class);

        TestObject obj = mapper.load(Map.of());
        assertNull(obj.test);
    }

    @Test
    void testNoArglessConstructor() {
        assertTrue(
            assertThrows(SerializeException.class, () -> {
                ObjectMapper<NonZeroArgConstructorObject> mapper = ObjectMapper.factory().get(NonZeroArgConstructorObject.class);
                mapper.load(Map.of());
            })
                .getMessage()
                .startsWith("Unable to create instances for this type")
        );
    }

    @Test
    void testSuperclassFieldsIncluded() throws SerializeException {
        ObjectMapper<TestObjectChild> mapper = ObjectMapper.factory().get(TestObjectChild.class);
        Map<String, Object> source = Map.of("childSetting", true, "test", "Parents get populated too");

        TestObjectChild instance = mapper.load(source);
        assertTrue(instance.childSetting);
        assertEquals("Parents get populated too", instance.test);
    }

    @Test
    void testInterfaceSerialization() throws SerializeException {
        ChildObject childObject = new ChildObject();
        childObject.test = "Changed value";

        ContainingObject containingObject = new ContainingObject();
        containingObject.list[0] = (childObject);
        containingObject.inner = childObject;

        ObjectMapper<ContainingObject> mapper = ObjectMapper.factory().get(ContainingObject.class);
        Map<String, Object> map = mapper.save(containingObject);

        ContainingObject newContainingObject = mapper.load(map);

        // serialization
        assertEquals(1, ((List<Map<String, Object>>) map.get("list")).size());
        assertEquals("Changed value", ((Map<String, Object>)map.get("inner")).get("test"));
        assertEquals("Changed value", ((List<Map<String, Object>>) map.get("list")).get(0).get("test"));
        assertEquals(ChildObject.class.getName(), ((Map<String, Object>)map.get("inner")).get("__class__"));
        assertEquals(ChildObject.class.getName(), ((List<Map<String, Object>>) map.get("list")).get(0).get("__class__"));

        // deserialization
        assertEquals(1, newContainingObject.list.length);
        assertEquals("Changed value", newContainingObject.inner.test());
        assertEquals("Changed value", newContainingObject.list[0].test());
    }

    private interface ParentInterface {
        String test();
    }

    private static class TestObject {
        protected String test;
    }

    private static class NonZeroArgConstructorObject {

        private final transient String value;
        private long key;

        protected NonZeroArgConstructorObject(final String value) {
            this.value = value;
        }
    }

    private static class TestObjectChild extends TestObject {
        private boolean childSetting;
    }

    private static class ChildObject implements ParentInterface {

        private String test = "Default value";

        @Override
        public String test() {
            return this.test;
        }
    }

    private static class ContainingObject {
        ParentInterface inner = new ChildObject();
        ParentInterface[] list = new ParentInterface[1];
    }
}
