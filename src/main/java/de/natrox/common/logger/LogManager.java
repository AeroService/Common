package de.natrox.common.logger;

import org.jetbrains.annotations.NotNull;

import java.util.ServiceLoader;

/**
 * The log manager provides static access to underlying api to shortcut logger creation methods for easier
 * accessibility.
 */
public final class LogManager {

    private static final LoggerFactory LOGGER_FACTORY = loadLoggerFactory();
    private static final String PROPERTY_KEY = "logger.debug";

    private LogManager() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the root logger for the system from the currently used logger factory.
     *
     * @return the root logger.
     */
    public static @NotNull Logger rootLogger() {
        return LogManager.logger(LoggerFactory.ROOT_LOGGER_NAME);
    }

    /**
     * Returns a logger from the current logger factory which has the name of the given class.
     *
     * @param caller the class to get the logger for.
     * @return a logger with the name of the given class.
     * @throws NullPointerException if the given class is null.
     */
    public static @NotNull Logger logger(@NotNull Class<?> caller) {
        return LogManager.logger(caller.getName());
    }

    /**
     * Returns a logger from the current logger factory which has the given name.
     *
     * @param name the name of the logger to get.
     * @return a logger with the given name.
     * @throws NullPointerException if the given name is null.
     */
    public static @NotNull Logger logger(@NotNull String name) {
        return LogManager.loggerFactory().logger(name);
    }

    /**
     * Returns the current logger factory which the system uses. The factory is statically initialized with the class and
     * will never change once initialized.
     *
     * @return the current selected logger factory.
     */
    public static @NotNull LoggerFactory loggerFactory() {
        return LOGGER_FACTORY;
    }

    /**
     * Toggles the debug mode. When debug mode is enabled, the {@see Logger#debug} method has effects and
     * messages will be logged. As an alternative to this method, {@code System.setProperty("logger.debug", true)} can be used.
     *
     * @param debug true if debug mode should be enabled and false if debug mode should be disabled
     */
    public static void setDebug(boolean debug) {
        System.setProperty(PROPERTY_KEY, String.valueOf(debug));
    }

    /**
     * Returns the current status of the debug mode, whether it is enabled or disabled. As an alternative to this method,
     * {@code Boolean.getBoolean("logger.debug")} can be used.
     *
     * @return Returns the current status of the debug mode
     */
    public static boolean debug() {
        return Boolean.getBoolean(PROPERTY_KEY);
    }

    /**
     * Selects the logger factory use. This method tries to load all providers for the logger factory class and uses the
     * first one which is available (if any) and uses it as the factory for loggers. If no logger factory service is
     * available on the class path this method falls back to the fallback logger factory which creates loggers wrapping
     * java.util.logging loggers.
     *
     * @return the logger factory to use in the runtime.
     * @throws java.util.ServiceConfigurationError if something went wrong during the service loading or instantiation.
     */
    private static @NotNull LoggerFactory loadLoggerFactory() {
        var factories = ServiceLoader.load(LoggerFactory.class).iterator();

        if (factories.hasNext()) {
            return factories.next();
        } else {
            return new FallbackLoggerFactory();
        }
    }
}
