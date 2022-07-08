package de.natrox.serialize;

public final class TypeSerializers {

    public final static TypeSerializer<Boolean> BOOLEAN = new BooleanSerializer();

    private TypeSerializers() {
        throw new UnsupportedOperationException();
    }

}
