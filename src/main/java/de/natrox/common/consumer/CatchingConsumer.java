package de.natrox.common.consumer;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Consumer that prints exceptions thrown
 */
public record CatchingConsumer<T>(Consumer<T> delegate) implements Consumer<T> {

    public CatchingConsumer(@NotNull Consumer<T> delegate) {
        Preconditions.checkNotNull(delegate, "delegate");
        this.delegate = delegate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(T t) {
        try {
            delegate.accept(t);
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
    }
}
