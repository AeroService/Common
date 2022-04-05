package de.natrox.common.function;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;

/**
 * Function that prints exceptions thrown
 */
@SuppressWarnings("ClassCanBeRecord")
public class CatchingFunction<T, R> implements Function<T, R> {

    private final Function<T, R> delegate;

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
