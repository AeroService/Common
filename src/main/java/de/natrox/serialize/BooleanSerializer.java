package de.natrox.serialize;

import de.natrox.serialize.exception.CoercionFailedException;
import de.natrox.serialize.exception.SerializeException;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.Locale;

final class BooleanSerializer extends TypeSerializer<Boolean> {

    BooleanSerializer() {
        super(Boolean.class);
    }

    @Override
    public @NotNull Boolean deserialize(@NotNull Object obj, @NotNull Type type) throws SerializeException {
        if (obj instanceof Number) {
            return !obj.equals(0);
        }

        final String potential = obj.toString().toLowerCase(Locale.ROOT);
        if (potential.equals("true")
            || potential.equals("t")
            || potential.equals("yes")
            || potential.equals("y")
            || potential.equals("1")) {
            return true;
        } else if (potential.equals("false")
            || potential.equals("f")
            || potential.equals("no")
            || potential.equals("n")
            || potential.equals("0")) {
            return false;
        }

        throw new CoercionFailedException(type, obj, "boolean");
    }
}
