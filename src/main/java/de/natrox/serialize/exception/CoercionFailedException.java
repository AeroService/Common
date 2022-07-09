package de.natrox.serialize.exception;

import java.lang.reflect.Type;

public class CoercionFailedException extends SerializeException {

    public CoercionFailedException(Object inputValue, String typeDescription) {
        super("Failed to coerce input value of type " + inputValue.getClass() + " to " + typeDescription);
    }

    public CoercionFailedException(Type target, Object inputValue, String typeDescription) {
        super(target, "Failed to coerce input value of type " + inputValue.getClass() + " to " + typeDescription);
    }
}
