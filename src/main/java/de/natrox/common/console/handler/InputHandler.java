package de.natrox.common.console.handler;

import org.jetbrains.annotations.NotNull;

public abstract class InputHandler extends Toggleable {

    public abstract void handleInput(@NotNull String line);

}
