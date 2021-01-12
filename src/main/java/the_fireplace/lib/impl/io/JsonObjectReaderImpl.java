package the_fireplace.lib.impl.io;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import the_fireplace.lib.api.io.JsonObjectReader;

import java.util.UUID;

public class JsonObjectReaderImpl implements JsonObjectReader {
    private final JsonObject obj;
    JsonObjectReaderImpl(JsonObject obj) {
        this.obj = obj;
    }

    @Override
    public JsonObject getJsonObject() {
        return obj;
    }

    @Override
    public UUID readUUID(String key, UUID ifAbsent) {
        if (obj.has(key)) {
            try {
                return UUID.fromString(obj.get(key).getAsString());
            } catch(IllegalArgumentException ignored) {}
        }
        return ifAbsent;
    }

    @Override
    public String readString(String key, String ifAbsent) {
        return obj.has(key) ? obj.get(key).getAsString() : ifAbsent;
    }

    @Override
    public long readLong(String key, long ifAbsent) {
        return obj.has(key) ? obj.get(key).getAsLong() : ifAbsent;
    }

    @Override
    public int readInt(String key, int ifAbsent) {
        return obj.has(key) ? obj.get(key).getAsInt() : ifAbsent;
    }

    @Override
    public short readShort(String key, short ifAbsent) {
        return obj.has(key) ? obj.get(key).getAsShort() : ifAbsent;
    }

    @Override
    public byte readByte(String key, byte ifAbsent) {
        return obj.has(key) ? obj.get(key).getAsByte() : ifAbsent;
    }

    @Override
    public double readDouble(String key, double ifAbsent) {
        return obj.has(key) ? obj.get(key).getAsDouble() : ifAbsent;
    }

    @Override
    public float readFloat(String key, float ifAbsent) {
        return obj.has(key) ? obj.get(key).getAsFloat() : ifAbsent;
    }

    @Override
    public boolean readBool(String key, boolean ifAbsent) {
        return obj.has(key) ? obj.get(key).getAsBoolean() : ifAbsent;
    }

    @Override
    public JsonArray readArray(String key) {
        return obj.has(key) ? obj.get(key).getAsJsonArray() : new JsonArray();
    }
}
