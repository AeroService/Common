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

package org.aero.common.event;

import org.aero.common.core.validate.Check;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EventBusTest {

    @Test
    void testCall() {
        final EventBus eventBus = EventBus.create();
        final AtomicBoolean result = new AtomicBoolean(false);

        final EventListener<EventTest> listener = EventListener.of(EventTest.class, eventTest -> result.set(true));

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
        final EventBus eventBus = EventBus.create();
        final AtomicBoolean result = new AtomicBoolean(false);

        final EventListener<CancellableTest> listener = EventListener
            .builder(CancellableTest.class)
            .handler(event -> {
                event.cancelled(true);
                result.set(true);
                assertTrue(event.isCancelled(), "The event should be cancelled");
            }).build();

        eventBus.register(listener);
        eventBus.call(new CancellableTest());
        assertTrue(result.get(), "The event should be called after the call");

        // Test cancelling
        eventBus.register(CancellableTest.class, event -> Check.fail("The event must have been cancelled"));
        eventBus.call(new CancellableTest());
    }

    @Test
    void testRecursive() {
        final EventBus eventBus = EventBus.create();
        final AtomicBoolean result = new AtomicBoolean(false);
        final AtomicBoolean result2 = new AtomicBoolean(false);

        final EventListener<Recursive> listener = EventListener.of(Recursive.class, event -> result.set(true));
        final EventListener<Recursive2> listener2 = EventListener.of(Recursive2.class, event -> result2.set(true));

        eventBus.register(listener);
        eventBus.register(listener2);

        eventBus.call(new Recursive2());
        assertTrue(result2.get(), "Recursive2 should have been called directly");
        assertTrue(result.get(),
            "Recursive should be called due to the fact that the Recursive2 event inherits from it");

        // Remove the direct listener
        result.set(false);
        result2.set(false);
        eventBus.unregister(listener2);
        eventBus.call(new Recursive2());
        assertFalse(result2.get(), "There is no listener for Recursive2");
        assertTrue(result.get(),
            "Recursive should be called due to the fact that the Recursive2 event inherits from it");
    }

    @Test
    void testPriorities() {
        final EventBus eventBus = EventBus.create();
        final AtomicInteger indicator = new AtomicInteger(0);

        final EventListener<EventTest> listener = EventListener
            .builder(EventTest.class)
            .priority(100)
            .handler(event -> {
                final int old = indicator.getAndSet(100);

                assertEquals(10, old);
            })
            .build();
        final EventListener<EventTest> listener2 = EventListener
            .builder(EventTest.class)
            .priority(10)
            .handler(event -> {
                final int old = indicator.getAndSet(10);

                assertEquals(5, old);
            })
            .build();

        eventBus.register(EventTest.class, event -> indicator.set(5));
        eventBus.register(listener);
        eventBus.register(listener2);

        assertEquals(0, indicator.get());
        eventBus.call(new EventTest());

        assertEquals(100, indicator.get());
    }

    static class EventTest {

    }

    static class CancellableTest extends AbstractCancellableEvent {

    }

    static class Recursive {

    }

    static class Recursive2 extends Recursive {

    }
}
