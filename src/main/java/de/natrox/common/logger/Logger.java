package de.natrox.common.logger;

import de.natrox.common.logger.dispatcher.LogRecordDispatcher;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.MissingResourceException;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Represents a logger that extends from the standard java.util.logging logger,
 * but provides methods for logging messages more easily, rather than wrapping them or using extensive method calls.
 */
public abstract class Logger extends java.util.logging.Logger {

    /**
     * Constructs a new logger instance.
     *
     * @param name               the name of the logger, null for anonymous loggers.
     * @param resourceBundleName the resource bundle name used for localizing messages passed to the logger, can be null.
     * @throws MissingResourceException if the resource bundle is given but no such bundle was found.
     */
    protected Logger(@Nullable String name, @Nullable String resourceBundleName) {
        super(name, resourceBundleName);
    }

    /**
     * Force logs the given record by directly flushing it to the underlying handlers. All other log methods will try to
     * pass the messages to the log record dispatcher first which processes them delayed.
     *
     * @param logRecord the record to log instantly.
     * @throws NullPointerException if the given record is null.
     */
    public abstract void forceLog(@NotNull LogRecord logRecord);

    /**
     * Get the log record dispatcher used by this logger. This method returns null if no dispatcher is set, meaning that
     * all log records which are created by this logger will be written to all handlers directly. If a dispatcher is
     * present all records will be posted to the dispatcher which is responsible to process and post them to all
     * registered handlers.
     *
     * @return the log record dispatcher used by this logger, null if no dispatcher is set.
     */
    public abstract @Nullable LogRecordDispatcher logRecordDispatcher();

    /**
     * Sets the log record dispatcher. If the given dispatcher is null all log records will be passed to the registered
     * handlers directly, if the dispatcher is given it will be responsible to process the given records and pass them to
     * all registered handlers.
     *
     * @param dispatcher the new dispatcher to use, null to remove the current log dispatcher.
     */
    public abstract void logRecordDispatcher(@Nullable LogRecordDispatcher dispatcher);

    /**
     * Logs the given message using the fine level. This method has no effect if the fine level is not enabled for this
     * logger and will not try to format the given message.
     * <p>
     * The message passed to this method must be in a valid formatter format to be formatted. If no arguments are given
     * the message will just get printed out as-is.
     * <p>
     * This method call is equivalent to {@code logger.log(Level.FINE, message, throwable, params)}.
     *
     * @param message   the message to print and format if parameters are given.
     * @param throwable the throwable associated with the current log event, can be null.
     * @param params    the params to format the given message with.
     * @throws NullPointerException             if the given message or an element of the given params array is null.
     * @throws java.util.IllegalFormatException if parameters are present but the given message uses an illegal format.
     */
    public void fine(@NotNull String message, @Nullable Throwable throwable, Object @NotNull ... params) {
        this.log(Level.FINE, message, throwable, params);
    }

    /**
     * Logs the given message using the finer level. This method has no effect if the fine level is not enabled for this
     * logger and will not try to format the given message.
     * <p>
     * The message passed to this method must be in a valid formatter format to be formatted. If no arguments are given
     * the message will just get printed out as-is.
     * <p>
     * This method call is equivalent to {@code logger.log(Level.FINER, message, throwable, params)}.
     *
     * @param message   the message to print and format if parameters are given.
     * @param throwable the throwable associated with the current log event, can be null.
     * @param params    the params to format the given message with.
     * @throws NullPointerException             if the given message or an element of the given params array is null.
     * @throws java.util.IllegalFormatException if parameters are present but the given message uses an illegal format.
     */
    public void finer(@NotNull String message, @Nullable Throwable throwable, Object @NotNull ... params) {
        this.log(Level.FINER, message, throwable, params);
    }

    /**
     * Logs the given message using the finest level. This method has no effect if the fine level is not enabled for this
     * logger and will not try to format the given message.
     * <p>
     * The message passed to this method must be in a valid formatter format to be formatted. If no arguments are given
     * the message will just get printed out as-is.
     * <p>
     * This method call is equivalent to {@code logger.log(Level.FINEST, message, throwable, params)}.
     *
     * @param message   the message to print and format if parameters are given.
     * @param throwable the throwable associated with the current log event, can be null.
     * @param params    the params to format the given message with.
     * @throws NullPointerException             if the given message or an element of the given params array is null.
     * @throws java.util.IllegalFormatException if parameters are present but the given message uses an illegal format.
     */
    public void finest(@NotNull String message, @Nullable Throwable throwable, Object @NotNull ... params) {
        this.log(Level.FINEST, message, throwable, params);
    }

    /**
     * Logs the given message using the severe level. This method has no effect if the fine level is not enabled for this
     * logger and will not try to format the given message.
     * <p>
     * The message passed to this method must be in a valid formatter format to be formatted. If no arguments are given
     * the message will just get printed out as-is.
     * <p>
     * This method call is equivalent to {@code logger.log(Level.SEVERE, message, throwable, params)}.
     *
     * @param message   the message to print and format if parameters are given.
     * @param throwable the throwable associated with the current log event, can be null.
     * @param params    the params to format the given message with.
     * @throws NullPointerException             if the given message or an element of the given params array is null.
     * @throws java.util.IllegalFormatException if parameters are present but the given message uses an illegal format.
     */
    public void severe(@NotNull String message, @Nullable Throwable throwable, Object @NotNull ... params) {
        this.log(Level.SEVERE, message, throwable, params);
    }

    /**
     * Logs the given message using the warning level. This method has no effect if the fine level is not enabled for this
     * logger and will not try to format the given message.
     * <p>
     * The message passed to this method must be in a valid formatter format to be formatted. If no arguments are given
     * the message will just get printed out as-is.
     * <p>
     * This method call is equivalent to {@code logger.log(Level.WARNING, message, throwable, params)}.
     *
     * @param message   the message to print and format if parameters are given.
     * @param throwable the throwable associated with the current log event, can be null.
     * @param params    the params to format the given message with.
     * @throws NullPointerException             if the given message or an element of the given params array is null.
     * @throws java.util.IllegalFormatException if parameters are present but the given message uses an illegal format.
     */
    public void warning(@NotNull String message, @Nullable Throwable throwable, Object @NotNull ... params) {
        this.log(Level.WARNING, message, throwable, params);
    }

    /**
     * Logs the given message using the info level. This method has no effect if the fine level is not enabled for this
     * logger and will not try to format the given message.
     * <p>
     * The message passed to this method must be in a valid formatter format to be formatted. If no arguments are given
     * the message will just get printed out as-is.
     * <p>
     * This method call is equivalent to {@code logger.log(Level.INFO, message, throwable, params)}.
     *
     * @param message   the message to print and format if parameters are given.
     * @param throwable the throwable associated with the current log event, can be null.
     * @param params    the params to format the given message with.
     * @throws NullPointerException             if the given message or an element of the given params array is null.
     * @throws java.util.IllegalFormatException if parameters are present but the given message uses an illegal format.
     */
    public void info(@NotNull String message, @Nullable Throwable throwable, Object @NotNull ... params) {
        this.log(Level.INFO, message, throwable, params);
    }

    /**
     * Logs the given message using the info level. This method has no effect if the fine level is not enabled for this
     * logger or the debug mode is not enabled {@see LogManager#debug}.
     * <p>
     * The message will just get printed out as-is.
     * <p>
     * This method call is equivalent to {@code logger.log(Level.INFO, message)}.
     *
     * @param message the message to print.
     * @throws NullPointerException if the given message is null.
     */
    public void debug(@NotNull String message) {
        if (LogManager.debug())
            this.log(Level.INFO, message);
    }

    /**
     * Logs the given message using the info level. This method has no effect if the fine level is not enabled for this
     * logger or the debug mode is not enabled {@see LogManager#debug} and will not try to format the given message.
     * <p>
     * The message passed to this method must be in a valid formatter format to be formatted. If no arguments are given
     * the message will just get printed out as-is.
     * <p>
     * This method call is equivalent to {@code logger.log(Level.INFO, message, throwable, params)}.
     *
     * @param message   the message to print and format if parameters are given.
     * @param throwable the throwable associated with the current log event, can be null.
     * @param params    the params to format the given message with.
     * @throws NullPointerException             if the given message or an element of the given params array is null.
     * @throws java.util.IllegalFormatException if parameters are present but the given message uses an illegal format.
     */
    public void debug(@NotNull String message, @Nullable Throwable throwable, Object @NotNull ... params) {
        if (LogManager.debug())
            this.log(Level.INFO, message, throwable, params);
    }

    /**
     * Logs the given message using the config level. This method has no effect if the fine level is not enabled for this
     * logger and will not try to format the given message.
     * <p>
     * The message passed to this method must be in a valid formatter format to be formatted. If no arguments are given
     * the message will just get printed out as-is.
     * <p>
     * This method call is equivalent to {@code logger.log(Level.CONFIG, message, throwable, params)}.
     *
     * @param message   the message to print and format if parameters are given.
     * @param throwable the throwable associated with the current log event, can be null.
     * @param params    the params to format the given message with.
     * @throws NullPointerException             if the given message or an element of the given params array is null.
     * @throws java.util.IllegalFormatException if parameters are present but the given message uses an illegal format.
     */
    public void config(@NotNull String message, @Nullable Throwable throwable, Object @NotNull ... params) {
        this.log(Level.CONFIG, message, throwable, params);
    }

    /**
     * Logs the given message using the provided level. This method has no effect if the fine level is not enabled for
     * this logger and will not try to format the given message.
     * <p>
     * The message passed to this method must be in a valid formatter format to be formatted. If no arguments are given
     * the message will just get printed out as-is.
     *
     * @param level   the level of the log record to log.
     * @param message the message to print and format if parameters are given.
     * @param th      the throwable associated with the current log event, can be null.
     * @param params  the params to format the given message with.
     * @throws NullPointerException             if the given level, message or an element of the params array is null.
     * @throws java.util.IllegalFormatException if parameters are present but the given message uses an illegal format.
     */
    public void log(@NotNull Level level, @NotNull String message, @Nullable Throwable th, Object @NotNull ... params) {
        if (this.isLoggable(level)) {
            this.log(level, params.length == 0 ? message : String.format(message, params), th);
        }
    }
}
