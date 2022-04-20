package de.natrox.common.function;

import org.jetbrains.annotations.UnknownNullability;

@FunctionalInterface
public interface ThrowableFunction<I, O, T extends Throwable> {

    @UnknownNullability O apply(@UnknownNullability I i) throws T;

}
