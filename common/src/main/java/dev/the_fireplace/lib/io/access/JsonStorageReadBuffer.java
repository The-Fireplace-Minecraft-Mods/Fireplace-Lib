package dev.the_fireplace.lib.io.access;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.the_fireplace.lib.FireplaceLibConstants;
import dev.the_fireplace.lib.api.io.interfaces.access.StorageReadBuffer;

import java.util.*;
import java.util.stream.Collectors;

public class JsonStorageReadBuffer implements StorageReadBuffer
{
    private final JsonObject obj;

    JsonStorageReadBuffer(JsonObject obj) {
        this.obj = obj;
    }

    @Override
    public UUID readUUID(String key, UUID ifAbsent) {
        if (obj.has(key)) {
            String uuidString = obj.get(key).getAsString();
            try {
                return UUID.fromString(uuidString);
            } catch (IllegalArgumentException e) {
                FireplaceLibConstants.getLogger().warn("Expected UUID for key '{}', got '{}'. Using default data instead.", key, uuidString);
            }
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
    public List<Boolean> readBoolList(String key, List<Boolean> ifAbsent) {
        if (!obj.has(key)) {
            return ifAbsent;
        }

        List<Boolean> list = new ArrayList<>();
        JsonArray array = obj.getAsJsonArray(key);
        for (JsonElement element : array) {
            list.add(element.getAsBoolean());
        }

        return list;
    }

    @Override
    public List<Number> readNumberList(String key, List<Number> ifAbsent) {
        if (!obj.has(key)) {
            return ifAbsent;
        }

        List<Number> list = new ArrayList<>();
        JsonArray array = obj.getAsJsonArray(key);
        for (JsonElement element : array) {
            list.add(element.getAsNumber());
        }

        return list;
    }

    @Override
    public List<String> readStringList(String key, List<String> ifAbsent) {
        if (!obj.has(key)) {
            return ifAbsent;
        }

        List<String> list = new ArrayList<>();
        JsonArray array = obj.getAsJsonArray(key);
        for (JsonElement element : array) {
            list.add(element.getAsString());
        }

        return list;
    }

    @Override
    public Map<String, String> readStringToStringMap(String key, Map<String, String> ifAbsent) {
        if (!obj.has(key)) {
            return ifAbsent;
        }

        Map<String, String> map = new HashMap<>();
        JsonArray array = obj.getAsJsonArray(key);
        for (JsonElement element : array) {
            JsonObject object = element.getAsJsonObject();
            map.put(object.get("key").getAsString(), object.get("value").getAsString());
        }

        return map;
    }

    @Override
    public Collection<String> getKeys() {
        return obj.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toSet());
    }
}
