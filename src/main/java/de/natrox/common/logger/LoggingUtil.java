package de.natrox.common.logger;

import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.LogRecord;

/**
 * Holds some utility methods to work with loggers.
 */
public final class LoggingUtil {

    private LoggingUtil() {
        throw new UnsupportedOperationException();
    }

    /**
     * Removes all registered handlers from the given logger.
     *
     * @param logger the logger to remove the handlers of.
     * @throws NullPointerException if the given logger is null.
     */
    public static void removeHandlers(@NotNull Logger logger) {
        for (var handler : logger.getHandlers()) {
            logger.removeHandler(handler);
        }
    }

    /**
     * Prints the throwable of the log record into the given string builder. If the given record has no associated
     * exception set this method does nothing.
     *
     * @param stringBuilder the string builder to print the exception to.
     * @param record        the record of which the exception should get printed into the builder.
     * @throws NullPointerException if the given string builder or log record is null.
     */
    public static void printStackTraceInto(@NotNull StringBuilder stringBuilder, @NotNull LogRecord record) {
        if (record.getThrown() != null) {
            var writer = new StringWriter();
            record.getThrown().printStackTrace(new PrintWriter(writer));
            stringBuilder.append('\n').append(writer);
        }
    }
}
