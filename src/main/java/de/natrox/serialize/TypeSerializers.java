package de.natrox.serialize;

public final class TypeSerializers {

    private TypeSerializers() {
        throw new UnsupportedOperationException();
    }

    public final static TypeSerializer<Boolean> BOOLEAN = new BooleanSerializer();

}
