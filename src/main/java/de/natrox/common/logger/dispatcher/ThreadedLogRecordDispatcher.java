package de.natrox.common.logger.dispatcher;

import de.natrox.common.logger.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.LogRecord;

public final class ThreadedLogRecordDispatcher extends Thread implements LogRecordDispatcher {

    public static final String THREAD_NAME_FORMAT = "Log record dispatcher %s";

    private final Logger logger;
    private final BlockingQueue<LogRecord> queue;

    private ThreadedLogRecordDispatcher(@NotNull Logger logger, @NotNull String threadName) {
        super(threadName);
        this.setDaemon(true);
        this.setPriority(Thread.MIN_PRIORITY);

        this.logger = logger;
        this.queue = new LinkedBlockingQueue<>();

        this.start();
    }

    public static @NotNull ThreadedLogRecordDispatcher forLogger(@NotNull Logger logger) {
        return ThreadedLogRecordDispatcher.newInstance(logger, String.format(THREAD_NAME_FORMAT, logger.getName()));
    }

    public static @NotNull ThreadedLogRecordDispatcher newInstance(@NotNull Logger logger, @NotNull String threadName) {
        return new ThreadedLogRecordDispatcher(logger, threadName);
    }

    @Override
    public void dispatchRecord(@NotNull Logger logger, @NotNull LogRecord record) {
        if (!super.isInterrupted()) {
            this.queue.add(record);
        }
    }

    @Override
    public void run() {
        while (!super.isInterrupted()) {
            try {
                var logRecord = this.queue.take();
                this.logger.forceLog(logRecord);
            } catch (InterruptedException exception) {
                break;
            }
        }

        for (var logRecord : this.queue) {
            this.logger.forceLog(logRecord);
        }
    }
}
