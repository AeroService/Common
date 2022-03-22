package de.natrox.common.logger;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface LoggerFactory {

    String ROOT_LOGGER_NAME = "";

    @NotNull Logger logger(@NotNull String name);

}
