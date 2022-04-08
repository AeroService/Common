package de.natrox.common.console;

import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

final class JLine3ConsoleBuilder implements Console.Builder {

    private Supplier<String> prompt;

    @Override
    public Console.@NotNull Builder prompt(@NotNull Supplier<String> prompt) {
        this.prompt = prompt;
        return this;
    }

    @Override
    public @NotNull Console build() throws Exception {
        return new JLine3Console(prompt);
    }
}
