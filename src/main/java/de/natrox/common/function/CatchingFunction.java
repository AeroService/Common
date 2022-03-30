package de.natrox.common.function;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;

/**
 * Function that prints exceptions thrown
 */
public record CatchingFunction<T, R>(Function<T, R> delegate) implements Function<T, R> {

    public CatchingFunction(@NotNull Function<T, R> delegate) {
        Objects.requireNonNull(delegate, "delegate can't be null!");
        this.delegate = delegate;
    }

    @Override
    public R apply(T t) {
        try {
            return delegate.apply(t);
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
    }
}
