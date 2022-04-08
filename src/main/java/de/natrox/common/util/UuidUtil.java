package de.natrox.common.util;

import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public final class UuidUtil {

    private UuidUtil() {
        throw new UnsupportedOperationException();
    }

    public static @NotNull UUID fromName(@NotNull String name) {
        return UUID.nameUUIDFromBytes(name.toLowerCase().getBytes(StandardCharsets.UTF_8));
    }
}
