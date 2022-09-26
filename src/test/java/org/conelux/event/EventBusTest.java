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

package org.conelux.event;

import static org.conelux.common.validate.Check.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class EventBusTest {

    @Test
    void testCall() {
        EventBus eventBus = EventBus.create();
        AtomicBoolean result = new AtomicBoolean(false);

        var listener = EventListener.of(EventTest.class, eventTest -> result.set(true));

        eventBus.register(listener);
        assertFalse(result.get(), "The event should not be called before the call");
        eventBus.call(new EventTest());
        assertTrue(result.get(), "The event should be called after the call");

        // Test removal
        result.set(false);
        eventBus.unregister(listener);
        eventBus.call(new EventTest());
        assertFalse(result.get(), "The event should not be called after the removal");
    }

    @Test
    void testCancellable() {
        EventBus eventBus = EventBus.create();
        AtomicBoolean result = new AtomicBoolean(false);

        var listener = EventListener
            .builder(CancellableTest.class)
            .handler(event -> {
                event.setCancelled(true);
                result.set(true);
                assertTrue(event.isCancelled(), "The event should be cancelled");
            }).build();

        eventBus.register(listener);
        eventBus.call(new CancellableTest());
        assertTrue(result.get(), "The event should be called after the call");

        // Test cancelling
        eventBus.register(CancellableTest.class, event -> fail("The event must have been cancelled"));
        eventBus.call(new CancellableTest());
    }

    @Test
    void testRecursive() {
        EventBus eventBus = EventBus.create();
        AtomicBoolean result1 = new AtomicBoolean(false);
        AtomicBoolean result2 = new AtomicBoolean(false);

        var listener1 = EventListener.of(Recursive1.class, event -> result1.set(true));
        var listener2 = EventListener.of(Recursive2.class, event -> result2.set(true));

        eventBus.register(listener1);
        eventBus.register(listener2);

        eventBus.call(new Recursive2());
        assertTrue(result2.get(), "Recursive2 should have been called directly");
        assertTrue(result1.get(),
            "Recursive1 should be called due to the fact that the Recursive2 event inherits from it");

        // Remove the direct listener
        result1.set(false);
        result2.set(false);
        eventBus.unregister(listener2);
        eventBus.call(new Recursive2());
        assertFalse(result2.get(), "There is no listener for Recursive2");
        assertTrue(result1.get(),
            "Recursive1 should be called due to the fact that the Recursive2 event inherits from it");
    }

    @Test
    void testPriorities() {
        EventBus eventBus = EventBus.create();
        AtomicInteger indicator = new AtomicInteger(0);

        var listener1 = EventListener
            .builder(EventTest.class)
            .priority(100)
            .handler(event -> {
                int old = indicator.getAndSet(100);

                assertEquals(10, old);
            })
            .build();
        var listener2 = EventListener
            .builder(EventTest.class)
            .priority(10)
            .handler(event -> {
                int old = indicator.getAndSet(10);

                assertEquals(5, old);
            })
            .build();

        eventBus.register(EventTest.class, event -> indicator.set(5));
        eventBus.register(listener1);
        eventBus.register(listener2);

        assertEquals(0, indicator.get());
        eventBus.call(new EventTest());

        assertEquals(100, indicator.get());
    }

    static class EventTest {

    }

    static class CancellableTest extends AbstractCancellableEvent {

    }

    static class Recursive1 {

    }

    static class Recursive2 extends Recursive1 {

    }

}
