package de.natrox.common.logger.handler;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * A log handler which automatically formats the given log record and notifies the provided message handler.
 */
public final class AcceptingLogHandler extends AbstractHandler {

    private final Consumer<String> handler;

    /**
     * Constructs a new accepting log handler instance.
     *
     * @param handler the consumer to post loggable, formatted log records to.
     * @throws NullPointerException if the given handler is null.
     */
    private AcceptingLogHandler(@NotNull Consumer<String> handler) {
        this.handler = handler;
        this.setLevel(Level.ALL);
    }

    /**
     * Constructs a new accepting log handler instance.
     *
     * @param handler the consumer to post loggable, formatted log records to.
     * @throws NullPointerException if the given handler is null.
     */
    public static @NotNull AcceptingLogHandler newInstance(@NotNull Consumer<String> handler) {
        return new AcceptingLogHandler(handler);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void publish(LogRecord record) {
        if (super.isLoggable(record)) {
            this.handler.accept(super.getFormatter().format(record));
        }
    }

    /**
     * Sets the formatter of this handler and returns the same instance as used to call the method, for chaining.
     *
     * @param formatter the formatter to use for this handler.
     * @return the same instance as used to call the method, for chaining.
     * @throws NullPointerException if the given formatter is null.
     */
    public @NotNull AcceptingLogHandler withFormatter(@NotNull Formatter formatter) {
        super.setFormatter(formatter);
        return this;
    }
}
