package de.natrox.common;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a class which is identified by a name.
 */
@FunctionalInterface
public interface Nameable {

    /**
     * Get the name of this instance.
     *
     * @return the name of this instance.
     */
    @NotNull String name();

}
