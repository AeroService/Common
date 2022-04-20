package de.natrox.common.function;

import org.jetbrains.annotations.UnknownNullability;

@FunctionalInterface
public interface ThrowableBiFunction<I, U, O, T extends Throwable> {

    @UnknownNullability O apply(@UnknownNullability I i, @UnknownNullability U u) throws T;

}
