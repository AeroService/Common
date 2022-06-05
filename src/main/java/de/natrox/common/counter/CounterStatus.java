package de.natrox.common.counter;

/**
 * Represents the different statuses for a {@link Counter}
 */
public enum CounterStatus {

    /**
     * The {@link Counter} is currently not running, it can be started at any given time.
     */
    IDLING,
    /**
     * The {@link Counter} is currently running and performing actions.
     * It can be interrupted or paused at any time.
     */
    RUNNING,
    /**
     * The {@link Counter} is currently paused, it is not performing any actions.
     * It can be resumed at any time.
     */
    PAUSED
}
