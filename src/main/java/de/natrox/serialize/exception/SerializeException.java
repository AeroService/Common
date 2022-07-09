package de.natrox.serialize.exception;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

public class SerializeException extends Exception {

    private @Nullable Type expectedType;

    public SerializeException() {

    }
    
    public SerializeException(String message) {
        super(message);
    }

    public SerializeException(Throwable cause) {
        super(cause);
    }

    public SerializeException(@Nullable Type expectedType, String message) {
        super(message);
        this.expectedType = expectedType;
    }

    public SerializeException(@Nullable Type expectedType, Throwable cause) {
        super(cause);
        this.expectedType = expectedType;
    }

    public SerializeException(@Nullable Type expectedType, String message, Throwable cause) {
        super(message, cause);
        this.expectedType = expectedType;
    }

    public @Nullable Type expectedType() {
        return this.expectedType;
    }
}
