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
class PairTest {

    @Test
    void testEquals() {
        final Pair<String, String> pair = Pair.of("first", "second");
        assertNotEquals(Pair.empty(), pair, "A not-empty Pair should not equal an empty Pair");
    }

    @Test
    void testEquals2() {
        final Pair<?, ?> pair = Pair.empty();
        assertEquals(Pair.empty(), pair, "An empty Pair should equal an empty Pair");
    }

    @Test
    void testEquals3() {
        final Pair<String, String> pair = Pair.of("first", "second");
        assertEquals(Pair.of("first", "second"), pair, "Two Pairs containing equal items should equal");
    }

    @Test
    void testEquals4() {
        assertNotEquals("o", Pair.empty(), "An empty Pair should not equal a String");
    }

    @Test
    void testEquals5() {
        final Pair<String, Pair<?, ?>> pair = Pair.of("first", Pair.empty());
        assertNotEquals(Pair.of("first", "second"), pair,
            "Two Pairs should only equal if both items of each are equal");
    }

    @Test
    void testEquals6() {
        final Pair<Pair<?, ?>, String> pair = Pair.of(Pair.empty(), "second");
        assertNotEquals(Pair.empty(), pair, "Two Pairs should only equal if both items of each are equal");
    }

    @Test
    void testEquals7() {
        final Pair<?, String> pair = Pair.of(null, "second");
        assertNotEquals(Pair.empty(), pair, "Two Pairs should only equal if both items of each are equal");
    }

    @Test
    void testSetFirst() {
        final Pair<String, String> pair = Pair.empty();
        pair.first("first");
        assertEquals("first", pair.first(),
            "Two Pairs should equal also if the first item is set after initialisation");
    }

    @Test
    void testSetSecond() {
        final Pair<String, String> pair = Pair.empty();
        pair.second("second");
        assertEquals("second", pair.second(),
            "Two Pairs should equal also if the second item is set after initialisation");
    }
}
