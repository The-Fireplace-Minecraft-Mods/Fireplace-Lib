package dev.the_fireplace.lib.api.storage.interfaces.access;

import java.util.Collection;
import java.util.UUID;

public interface StorageReadBuffer {
    UUID readUUID(String key, UUID ifAbsent);
    String readString(String key, String ifAbsent);
    long readLong(String key, long ifAbsent);
    int readInt(String key, int ifAbsent);
    short readShort(String key, short ifAbsent);
    byte readByte(String key, byte ifAbsent);
    double readDouble(String key, double ifAbsent);
    float readFloat(String key, float ifAbsent);
    boolean readBool(String key, boolean ifAbsent);
    Collection<String> getKeys();
}
