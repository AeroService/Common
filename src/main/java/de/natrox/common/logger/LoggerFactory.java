package de.natrox.common.logger;

import org.jetbrains.annotations.NotNull;

/**
 * A factory for logger objects.
 */
@FunctionalInterface
public interface LoggerFactory {

    /**
     * The name of the root logger used by the system. Editing the associated logger will result in all loggers to
     * change.
     */
    String ROOT_LOGGER_NAME = "";

    /**
     * Gets or creates a logger with the given name. Overridden methods may but must no cache created logger objects,
     * meaning that this method can also create a new logger each time this method is called.
     *
     * @param name the name of the logger.
     * @return the cached or created logger object having the given name.
     * @throws NullPointerException if the given name is null.
     */
    @NotNull Logger logger(@NotNull String name);

}
