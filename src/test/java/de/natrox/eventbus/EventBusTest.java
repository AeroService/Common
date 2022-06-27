package de.natrox.eventbus;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static de.natrox.common.validate.Check.fail;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void testCancellable() {
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

    static class EventTest {

    }

    static class CancellableTest extends AbstractCancellableEvent {

    }

}
