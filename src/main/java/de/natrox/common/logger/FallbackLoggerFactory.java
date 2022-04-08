package de.natrox.common.logger;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.LogManager;

/**
 * The fallback logger factory which creates the loggers based on java.util.logging loggers which get wrapped into
 * custom loggers. This factory caches the logger instances which means that demanding a logger with the same name twice
 * results in the same logger instance each time.
 */
final class FallbackLoggerFactory implements LoggerFactory {

    private final Map<String, Logger> createdLoggers = new ConcurrentHashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Logger logger(@NotNull String name) {
        var registered = LogManager.getLogManager().getLogger(name);

        if (registered instanceof Logger) {
            return (Logger) registered;
        }

        return this.createdLoggers.computeIfAbsent(name, $ -> {
            if (registered == null) {

                var julComputedLoggerInstance = Logger.getLogger(name);
                return new FallbackPassthroughLogger(julComputedLoggerInstance);
            } else {

                return new FallbackPassthroughLogger(registered);
            }
        });
    }
}
