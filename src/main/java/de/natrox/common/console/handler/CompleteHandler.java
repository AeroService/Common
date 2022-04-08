package de.natrox.common.console.handler;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public abstract class CompleteHandler extends Toggleable {

    public abstract @NotNull Collection<String> completeInput(@NotNull String line);

}
