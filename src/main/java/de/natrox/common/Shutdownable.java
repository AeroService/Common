package de.natrox.common;

/**
 * Represents a class that can be shut down.
 */
@FunctionalInterface
public interface Shutdownable {

    void shutdown();

}
