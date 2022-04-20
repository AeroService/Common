package de.natrox.common.consumer;

import org.jetbrains.annotations.UnknownNullability;

@FunctionalInterface
public interface ThrowableConsumer<E, T extends Throwable> {

    void accept(@UnknownNullability E element) throws T;

}
