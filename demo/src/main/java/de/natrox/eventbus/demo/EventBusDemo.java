package de.natrox.eventbus.demo;

import de.natrox.eventbus.EventBus;
import de.natrox.eventbus.EventListener;

public class EventBusDemo {

    public static void main(String[] args) {
        EventBus eventBus = EventBus.create();

        EventListener<TestEvent> listener = EventListener
            .builder(TestEvent.class)
            .condition(TestEvent::print)
            .handler(event -> System.out.println(event.message()))
            .build();

        eventBus.register(listener);
        eventBus.call(new TestEvent("Hello World!", true));
    }
}
