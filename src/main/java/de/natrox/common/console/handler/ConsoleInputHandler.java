package de.natrox.common.console.handler;

import org.jetbrains.annotations.NotNull;

public abstract class ConsoleInputHandler extends Toggleable {

    public abstract void handleInput(@NotNull String line);

}
