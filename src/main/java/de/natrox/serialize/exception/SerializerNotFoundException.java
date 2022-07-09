package de.natrox.serialize.exception;

import java.lang.reflect.Type;
import java.util.Arrays;

public class SerializerNotFoundException extends SerializeException {

    public SerializerNotFoundException(Object inputValue, Type... targets) {
        super("Failed to find serializer which coerces the input value of type " + inputValue.getClass() + " to one of this types "
            + Arrays.toString(Arrays.stream(targets).map(Type::getTypeName).toArray()));
    }
}
