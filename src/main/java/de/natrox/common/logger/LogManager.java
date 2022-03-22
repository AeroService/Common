package de.natrox.common.logger;

import org.jetbrains.annotations.NotNull;

import java.util.ServiceLoader;

public final class LogManager {

    private static final LoggerFactory LOGGER_FACTORY = loadLoggerFactory();

    private LogManager() {
        throw new UnsupportedOperationException();
    }

    public static @NotNull Logger rootLogger() {
        return LogManager.logger(LoggerFactory.ROOT_LOGGER_NAME);
    }

    public static @NotNull Logger logger(@NotNull Class<?> caller) {
        return LogManager.logger(caller.getName());
    }

    public static @NotNull Logger logger(@NotNull String name) {
        return LogManager.loggerFactory().logger(name);
    }

    public static @NotNull LoggerFactory loggerFactory() {
        return LOGGER_FACTORY;
    }

    private static @NotNull LoggerFactory loadLoggerFactory() {
        var factories = ServiceLoader.load(LoggerFactory.class).iterator();

        if (factories.hasNext()) {
            return factories.next();
        } else {
            return new FallbackLoggerFactory();
        }
    }
}
