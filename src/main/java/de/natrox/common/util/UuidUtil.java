package de.natrox.common.util;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public final class UuidUtil {

    private UuidUtil() {
        throw new UnsupportedOperationException();
    }

    public static UUID fromName(String name) {
        return UUID.nameUUIDFromBytes(name.toLowerCase().getBytes(StandardCharsets.UTF_8));
    }
}
