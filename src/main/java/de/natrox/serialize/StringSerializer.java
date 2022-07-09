package de.natrox.serialize;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

final class StringSerializer extends TypeSerializer<String> {

    StringSerializer() {
        super(String.class);
    }

    @Override
    public @NotNull String deserialize(@NotNull Object obj, @NotNull Type type) {
        return obj.toString();
    }
}
