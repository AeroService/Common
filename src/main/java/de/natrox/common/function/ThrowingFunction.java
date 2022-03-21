package de.natrox.common.function;

import java.util.function.Function;

/**
 * Represents a function that accepts two arguments and produces a result. This is a throwing specialization of
 * {@link Function}.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #apply(Object)}.
 *
 * @param <T> the type of the argument to the function
 * @param <R> the type of the result of the function
 * @see Function
 */
public interface ThrowingFunction<R, T, E extends Exception> {

    /**
     * Applies this function to the given arguments.
     *
     * @param t the input argument
     * @return the function result
     */
    R apply(T t) throws E;

}