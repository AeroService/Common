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

package org.aero.common.core.consumer;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CatchingConsumerTest {

    @Test
    void testAccept() {
        final AtomicInteger indicator = new AtomicInteger();
        final CatchingConsumer<Integer> consumer = new CatchingConsumer<>(indicator::set);
        consumer.accept(1);
        assertEquals(1, indicator.get(), "Indicator should indicate the input of 1");
        consumer.accept(2);
        assertEquals(2, indicator.get(), "Indicator should indicate the input of 2");
    }

    @Test
    void testThrowingAccept() {
        final CatchingConsumer<Integer> consumer = new CatchingConsumer<>(i -> this.throwException());
        assertThrows(IllegalArgumentException.class, () ->
            consumer.accept(-1), "Consumer should throw an exception if the arguments don't meet the conditions");
    }

    private void throwException() {
        throw new IllegalArgumentException();
    }
}
