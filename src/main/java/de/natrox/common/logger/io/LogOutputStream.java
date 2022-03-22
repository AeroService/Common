package de.natrox.common.logger.io;

import de.natrox.common.logger.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;

public final class LogOutputStream extends ByteArrayOutputStream {

    public static boolean DEFAULT_AUTO_FLUSH = true;
    public static Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private final Level level;
    private final Logger logger;
    private final Lock flushLock;

    private LogOutputStream(@NotNull Level level, @NotNull Logger logger) {
        this.level = level;
        this.logger = logger;
        this.flushLock = new ReentrantLock();
    }

    public static @NotNull LogOutputStream forSevere(@NotNull Logger logger) {
        return LogOutputStream.newInstance(logger, Level.SEVERE);
    }

    public static @NotNull LogOutputStream forInformative(@NotNull Logger logger) {
        return LogOutputStream.newInstance(logger, Level.INFO);
    }

    public static @NotNull LogOutputStream newInstance(@NotNull Logger logger, @NotNull Level level) {
        return new LogOutputStream(level, logger);
    }

    public @NotNull PrintStream toPrintStream() {
        return this.toPrintStream(DEFAULT_AUTO_FLUSH, DEFAULT_CHARSET);
    }

    public @NotNull PrintStream toPrintStream(boolean autoFlush, @NotNull Charset charset) {
        return new PrintStream(this, autoFlush, charset);
    }

    @Override
    public void flush() throws IOException {
        this.flushLock.lock();
        try {
            super.flush();
            var content = this.toString(StandardCharsets.UTF_8.name());
            super.reset();

            if (!content.isEmpty() && !content.equals(System.lineSeparator())) {
                this.logger.log(this.level, content);
            }
        } finally {
            this.flushLock.unlock();
        }
    }
}
