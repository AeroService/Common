package de.natrox.common.logger.handler;

import java.util.logging.Handler;

public abstract class AbstractHandler extends Handler {

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
    }
}
