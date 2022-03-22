package de.natrox.common.logger;

import de.natrox.common.logger.dispatcher.LogRecordDispatcher;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ResourceBundle;
import java.util.logging.Filter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

final class FallbackPassthroughLogger extends Logger {

    private final java.util.logging.Logger logger;
    private LogRecordDispatcher logRecordDispatcher;

    FallbackPassthroughLogger(@NotNull java.util.logging.Logger logger) {
        super(logger.getName(), logger.getResourceBundleName());
        this.logger = logger;
    }

    @Override
    public void log(LogRecord record) {
        if (this.logRecordDispatcher == null) {
            this.logger.log(record);
        } else {
            this.logRecordDispatcher.dispatchRecord(this, record);
        }
    }

    @Override
    public void forceLog(@NotNull LogRecord logRecord) {
        this.logger.log(logRecord);
    }

    @Override
    public @Nullable LogRecordDispatcher logRecordDispatcher() {
        return this.logRecordDispatcher;
    }

    @Override
    public void logRecordDispatcher(@Nullable LogRecordDispatcher dispatcher) {
        this.logRecordDispatcher = dispatcher;
    }

    @Override
    public ResourceBundle getResourceBundle() {
        return this.logger.getResourceBundle();
    }

    @Override
    public void setResourceBundle(ResourceBundle bundle) {
        this.logger.setResourceBundle(bundle);
    }

    @Override
    public String getResourceBundleName() {
        return this.logger.getResourceBundleName();
    }

    @Override
    public Filter getFilter() {
        return this.logger.getFilter();
    }

    @Override
    public void setFilter(Filter newFilter) throws SecurityException {
        this.logger.setFilter(newFilter);
    }

    @Override
    public Level getLevel() {
        var thisLevel = this.logger.getLevel();
        return thisLevel == null ? LogManager.rootLogger().getLevel() : thisLevel;
    }

    @Override
    public void setLevel(Level newLevel) throws SecurityException {
        this.logger.setLevel(newLevel);
    }

    @Override
    public boolean isLoggable(Level level) {
        return this.logger.isLoggable(level);
    }

    @Override
    public void addHandler(Handler handler) throws SecurityException {
        this.logger.addHandler(handler);
    }

    @Override
    public void removeHandler(Handler handler) throws SecurityException {
        this.logger.removeHandler(handler);
    }

    @Override
    public Handler[] getHandlers() {
        return this.logger.getHandlers();
    }

    @Override
    public boolean getUseParentHandlers() {
        return this.logger.getUseParentHandlers();
    }

    @Override
    public void setUseParentHandlers(boolean useParentHandlers) {
        this.logger.setUseParentHandlers(useParentHandlers);
    }

    @Override
    public java.util.logging.Logger getParent() {
        return this.logger.getParent();
    }

    @Override
    public void setParent(java.util.logging.Logger parent) {
        this.logger.setParent(parent);
    }
}
