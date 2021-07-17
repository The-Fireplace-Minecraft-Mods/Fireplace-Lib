package dev.the_fireplace.lib.api.io.interfaces.access;

import java.util.UUID;

public interface SimpleBuffer {
    UUID readUUID(String key);
    String readString(String key);
    long readLong(String key);
    int readInt(String key);
    short readShort(String key);
    byte readByte(String key);
    double readDouble(String key);
    float readFloat(String key);
    boolean readBool(String key);
    boolean hasKey(String key);
}
