package de.notion.common.runnable;

/**
 * This is a throwing specialization of
 * {@link Runnable}.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #run()}.
 *
 * @see Runnable
 */
public interface ThrowingRunnable {

    /**
     * @throws Exception
     */
    void run() throws Exception;

}
