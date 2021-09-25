package dev.the_fireplace.lib.api.io.interfaces.access;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface StorageWriteBuffer {
    void writeUUID(String key, UUID value);

    void writeString(String key, String value);

    void writeLong(String key, long value);

    void writeInt(String key, int value);

    void writeShort(String key, short value);

    void writeByte(String key, byte value);

    void writeDouble(String key, double value);

    void writeFloat(String key, float value);

    void writeBool(String key, boolean value);

    void writeBoolList(String key, List<Boolean> values);

    void writeNumberList(String key, List<Number> values);

    void writeStringList(String key, List<String> values);

    void writeStringToStringMap(String key, Map<String, String> values);
}
