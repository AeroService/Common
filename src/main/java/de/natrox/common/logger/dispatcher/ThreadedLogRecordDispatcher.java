package de.natrox.common.logger.dispatcher;

import de.natrox.common.logger.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.LogRecord;

/**
 * A log record dispatcher which dispatches requested log records on a single thread one at a time.
 */
public final class ThreadedLogRecordDispatcher extends Thread implements LogRecordDispatcher {

    public static final String THREAD_NAME_FORMAT = "Log record dispatcher %s";

    private final Logger logger;
    private final BlockingQueue<LogRecord> queue;

    /**
     * Constructs a new threaded log record dispatcher instance. This automatically starts the thread.
     *
     * @param logger     the logger to which log records should get logged.
     * @param threadName the name of the thread to use.
     * @throws NullPointerException if the given logger or thread name is null.
     */
    private ThreadedLogRecordDispatcher(@NotNull Logger logger, @NotNull String threadName) {
        super(threadName);
        this.setDaemon(true);
        this.setPriority(Thread.MIN_PRIORITY);

        this.logger = logger;
        this.queue = new LinkedBlockingQueue<>();

        this.start();
    }

    /**
     * Creates a new threaded log record dispatcher instance using the given logger as the target and formatting the
     * default thread name format using the given loggers name.
     *
     * @param logger the logger this dispatcher should pump requests to.
     * @return a new threaded log record dispatcher instance.
     * @throws NullPointerException if the given logger is null.
     */
    public static @NotNull ThreadedLogRecordDispatcher forLogger(@NotNull Logger logger) {
        return ThreadedLogRecordDispatcher.newInstance(logger, String.format(THREAD_NAME_FORMAT, logger.getName()));
    }

    /**
     * Creates a new threaded log record dispatcher instance using the given logger as the target and the given thread
     * name.
     *
     * @param logger     the logger this dispatcher should pump requests to.
     * @param threadName the name of the dispatcher thread to use.
     * @return a new threaded log record dispatcher instance.
     * @throws NullPointerException if the given logger or thread name is null.
     */
    public static @NotNull ThreadedLogRecordDispatcher newInstance(@NotNull Logger logger, @NotNull String threadName) {
        return new ThreadedLogRecordDispatcher(logger, threadName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispatchRecord(@NotNull Logger logger, @NotNull LogRecord record) {
        if (!super.isInterrupted()) {
            this.queue.add(record);
        }
    }

    /**
     * {@inheritDoc}
     */
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
