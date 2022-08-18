package de.natrox.event.demo;

import de.natrox.event.AbstractCancellableEvent;

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
