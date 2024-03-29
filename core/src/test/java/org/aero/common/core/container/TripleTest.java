/*
 * Copyright 2020-2023 AeroService
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

package org.aero.common.core.container;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SuppressWarnings("AssertBetweenInconvertibleTypes")
class TripleTest {

    @Test
    void testEquals() {
        final Triple<String, String, String> triple = Triple.of("first", "second", "third");
        assertNotEquals(triple, Triple.empty(), "A not-empty Triple should not equal an empty Triple");
    }

    @Test
    void testEquals2() {
        final Triple<?, ?, ?> triple = Triple.empty();
        assertEquals(Triple.empty(), triple, "An empty Triple should equal an empty Triple");
    }

    @Test
    void testEquals3() {
        final Triple<String, String, String> triple = Triple.of("first", "second", "third");
        assertEquals(Triple.of("first", "second", "third"), triple, "Two Triples containing equal items should equal");
    }

    @Test
    void testEquals4() {
        assertNotEquals("o", Triple.empty(), "An empty Triple should not equal a String");
    }

    @Test
    void testEquals5() {
        final Triple<String, String, Triple<?, ?, ?>> triple = Triple.of("first", "second", Triple.empty());
        assertNotEquals(Triple.of("first", "second", "third"), triple,
            "Two Triples should only equal if both items of each are equal");
    }

    @Test
    void testEquals6() {
        final Triple<Triple<?, ?, ?>, String, String> triple = Triple.of(Triple.empty(), "second", "third");
        assertNotEquals(Triple.empty(), triple, "Two Triples should only equal if both items of each are equal");
    }

    @Test
    void testEquals7() {
        final Triple<?, String, String> triple = Triple.of(null, "second", "third");
        assertNotEquals(Triple.empty(), triple, "Two Triples should only equal if both items of each are equal");
    }

    @Test
    void testSetFirst() {
        final Triple<String, String, String> triple = Triple.empty();
        triple.first("first");
        assertEquals("first", triple.first(),
            "Two Triples should equal also if the first item is set after initialisation");
    }

    @Test
    void testSetSecond() {
        final Triple<String, String, String> triple = Triple.empty();
        triple.second("second");
        assertEquals("second", triple.second(),
            "Two Triples should equal also if the second item is set after initialisation");
    }

    @Test
    void testSetThird() {
        final Triple<String, String, String> triple = Triple.empty();
        triple.third("third");
        assertEquals("third", triple.third(),
            "Two Triples should equal also if the second item is set after initialisation");
    }
}
