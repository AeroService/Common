package de.natrox.common.supplier;

import org.jetbrains.annotations.UnknownNullability;

@FunctionalInterface
public interface ThrowableSupplier<O, T extends Throwable> {

    @UnknownNullability O get() throws T;

}
