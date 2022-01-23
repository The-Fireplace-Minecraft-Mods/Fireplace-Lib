package dev.the_fireplace.lib.api.io.interfaces.access;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface StorageReadBuffer
{
    UUID readUUID(String key, UUID ifAbsent);

    String readString(String key, String ifAbsent);

    long readLong(String key, long ifAbsent);

    int readInt(String key, int ifAbsent);

    short readShort(String key, short ifAbsent);

    byte readByte(String key, byte ifAbsent);

    double readDouble(String key, double ifAbsent);

    float readFloat(String key, float ifAbsent);

    boolean readBool(String key, boolean ifAbsent);

    List<Boolean> readBoolList(String key, List<Boolean> ifAbsent);

    List<Number> readNumberList(String key, List<Number> ifAbsent);

    List<String> readStringList(String key, List<String> ifAbsent);

    Map<String, String> readStringToStringMap(String key, Map<String, String> ifAbsent);

    Collection<String> getKeys();
}
