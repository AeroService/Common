/*
 * Copyright 2020-2022 NatroxMC
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.natrox.event;

import static de.natrox.common.validate.Check.fail;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Test;

class EventBusTest {

    @Test
    void callTest() {
        EventBus eventBus = EventBus.create();

        AtomicBoolean result = new AtomicBoolean(false);
        EventListener<?> listener = EventListener.of(EventTest.class, eventTest -> result.set(true));
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
    void cancellableTest() {
        EventBus eventBus = EventBus.create();
        AtomicBoolean result = new AtomicBoolean(false);
        EventListener<?> listener = EventListener
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
    void recursiveTest() {
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

    static class EventTest {

    }

    static class CancellableTest extends AbstractCancellableEvent {

    }

    static class Recursive1 {

    }

    static class Recursive2 extends Recursive1 {

    }

}
