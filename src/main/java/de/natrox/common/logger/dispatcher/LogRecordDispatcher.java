package de.natrox.common.logger.dispatcher;

import de.natrox.common.logger.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.logging.LogRecord;

/**
 * Represents a dispatcher for log records. This dispatcher is meant to pre-process log records if needed and then post
 * them to all registered dispatchers of a logger, for example from an asynchronous context.
 */
@FunctionalInterface
public interface LogRecordDispatcher {

    /**
     * Called when a log record needs to be dispatched for a logger.
     *
     * @param logger the logger from which the log event came.
     * @param record the record which needs to be dispatched.
     * @throws NullPointerException if either the given logger or record is null.
     */
    void dispatchRecord(@NotNull Logger logger, @NotNull LogRecord record);
}
