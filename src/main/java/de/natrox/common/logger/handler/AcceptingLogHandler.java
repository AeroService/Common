package de.natrox.common.logger.handler;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public final class AcceptingLogHandler extends AbstractHandler {

    private final Consumer<String> handler;

    private AcceptingLogHandler(@NotNull Consumer<String> handler) {
        this.handler = handler;
        this.setLevel(Level.ALL);
    }

    public static @NotNull AcceptingLogHandler newInstance(@NotNull Consumer<String> handler) {
        return new AcceptingLogHandler(handler);
    }

    @Override
    public void publish(LogRecord record) {
        if (super.isLoggable(record)) {
            this.handler.accept(super.getFormatter().format(record));
        }
    }

    public @NotNull AcceptingLogHandler withFormatter(@NotNull Formatter formatter) {
        super.setFormatter(formatter);
        return this;
    }
}
