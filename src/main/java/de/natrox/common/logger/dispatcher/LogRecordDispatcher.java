package de.natrox.common.logger.dispatcher;

import de.natrox.common.logger.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.logging.LogRecord;

@FunctionalInterface
public interface LogRecordDispatcher {

    void dispatchRecord(@NotNull Logger logger, @NotNull LogRecord record);
}
