package de.natrox.common.logger;

import de.natrox.common.logger.dispatcher.LogRecordDispatcher;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;
import java.util.logging.LogRecord;

public abstract class Logger extends java.util.logging.Logger {

    protected Logger(@Nullable String name, @Nullable String resourceBundleName) {
        super(name, resourceBundleName);
    }

    public abstract void forceLog(@NotNull LogRecord logRecord);

    public abstract @Nullable LogRecordDispatcher logRecordDispatcher();

    public abstract void logRecordDispatcher(@Nullable LogRecordDispatcher dispatcher);

    public void fine(@NotNull String message, @Nullable Throwable throwable, Object @NotNull ... params) {
        this.log(Level.FINE, message, throwable, params);
    }

    public void finer(@NotNull String message, @Nullable Throwable throwable, Object @NotNull ... params) {
        this.log(Level.FINER, message, throwable, params);
    }

    public void finest(@NotNull String message, @Nullable Throwable throwable, Object @NotNull ... params) {
        this.log(Level.FINEST, message, throwable, params);
    }

    public void severe(@NotNull String message, @Nullable Throwable throwable, Object @NotNull ... params) {
        this.log(Level.SEVERE, message, throwable, params);
    }

    public void warning(@NotNull String message, @Nullable Throwable throwable, Object @NotNull ... params) {
        this.log(Level.WARNING, message, throwable, params);
    }

    public void info(@NotNull String message, @Nullable Throwable throwable, Object @NotNull ... params) {
        this.log(Level.INFO, message, throwable, params);
    }

    public void config(@NotNull String message, @Nullable Throwable throwable, Object @NotNull ... params) {
        this.log(Level.CONFIG, message, throwable, params);
    }

    public void log(@NotNull Level level, @NotNull String message, @Nullable Throwable th, Object @NotNull ... params) {
        if (this.isLoggable(level)) {
            this.log(level, params.length == 0 ? message : String.format(message, params), th);
        }
    }
}
