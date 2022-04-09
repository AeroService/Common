package de.natrox.common.function;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;

/**
 * Function that prints exceptions thrown
 */
public record CatchingFunction<T, R>(Function<T, R> delegate) implements Function<T, R> {

    public CatchingFunction(@NotNull Function<T, R> delegate) {
        Preconditions.checkNotNull(delegate, "delegate");
        this.delegate = delegate;
    }

    /**
     * {@inheritDoc}
     */
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
