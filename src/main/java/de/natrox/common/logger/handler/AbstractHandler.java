package de.natrox.common.logger.handler;

import java.util.logging.Handler;

/**
 * An abstract handler implementation which only requires the extending class to overwrite {@code publish} instead of
 * implementing the (mostly) unused methods flush and close as well.
 */
public abstract class AbstractHandler extends Handler {

    /**
     * {@inheritDoc}
     */
    @Override
    public void flush() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws SecurityException {
    }
}
