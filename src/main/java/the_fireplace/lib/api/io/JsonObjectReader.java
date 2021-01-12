package the_fireplace.lib.api.io;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.UUID;

public interface JsonObjectReader {
    JsonObject getJsonObject();

    UUID readUUID(String key, UUID ifAbsent);
    String readString(String key, String ifAbsent);
    long readLong(String key, long ifAbsent);
    int readInt(String key, int ifAbsent);
    short readShort(String key, short ifAbsent);
    byte readByte(String key, byte ifAbsent);
    double readDouble(String key, double ifAbsent);
    float readFloat(String key, float ifAbsent);
    boolean readBool(String key, boolean ifAbsent);
    JsonArray readArray(String key);
}
