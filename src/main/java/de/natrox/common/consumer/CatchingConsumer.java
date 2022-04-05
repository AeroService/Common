package de.natrox.common.consumer;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Consumer that prints exceptions thrown
 */
public record CatchingConsumer<T>(Consumer<T> delegate) implements Consumer<T> {

    public CatchingConsumer(@NotNull Consumer<T> delegate) {
        Objects.requireNonNull(delegate, "delegate can't be null!");
        this.delegate = delegate;
    }

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
