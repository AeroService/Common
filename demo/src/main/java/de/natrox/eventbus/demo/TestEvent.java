package de.natrox.eventbus.demo;

import de.natrox.eventbus.AbstractCancellableEvent;

public final class TestEvent extends AbstractCancellableEvent {

    private final String message;
    private final boolean print;

    public TestEvent(String message, boolean print) {
        this.message = message;
        this.print = print;
    }

    public String message() {
        return this.message;
    }

    public boolean print() {
        return this.print;
    }
}
