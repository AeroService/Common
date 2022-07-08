package de.natrox.serialize;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.function.Predicate;

final class BooleanSerializer extends TypeSerializer<Boolean> {

    BooleanSerializer() {
        super(Boolean.class);
    }

    @Override
    public Object serialize(Boolean value, Predicate<Class<?>> types) {
        if (types.test(Integer.class)) {
            return value ? 1 : 0;
        } else {
            return value.toString();
        }
    }

    @Override
    public Boolean deserialize(Type type, Object obj) {
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

        throw new RuntimeException();
    }
}
