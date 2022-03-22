package de.natrox.common.logger;

import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.LogRecord;

public final class LoggingUtil {

    private LoggingUtil() {
        throw new UnsupportedOperationException();
    }

    public static void removeHandlers(@NotNull Logger logger) {
        for (var handler : logger.getHandlers()) {
            logger.removeHandler(handler);
        }
    }

    public static void printStackTraceInto(@NotNull StringBuilder stringBuilder, @NotNull LogRecord record) {
        if (record.getThrown() != null) {
            var writer = new StringWriter();
            record.getThrown().printStackTrace(new PrintWriter(writer));
            stringBuilder.append('\n').append(writer);
        }
    }
}
