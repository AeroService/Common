package de.natrox.serialize;

public final class TypeSerializers {

    public final static TypeSerializer<Boolean> BOOLEAN = new BooleanSerializer();

    public final static TypeSerializer<String> STRING = new StringSerializer();

    private TypeSerializers() {
        throw new UnsupportedOperationException();
    }
}
