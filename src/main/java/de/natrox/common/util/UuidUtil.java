package de.natrox.common.util;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Holds some utility methods to work with uuids.
 */
public final class UuidUtil {

    private UuidUtil() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the {@link UUID} of a string.
     *
     * @param name the string for the generation of the {@link UUID}
     * @return the {@link UUID} of the specified string
     */
    public static @NotNull UUID fromName(@NotNull String name) {
        Preconditions.checkNotNull(name, "name");
        return UUID.nameUUIDFromBytes(name.toLowerCase().getBytes(StandardCharsets.UTF_8));
    }
}
