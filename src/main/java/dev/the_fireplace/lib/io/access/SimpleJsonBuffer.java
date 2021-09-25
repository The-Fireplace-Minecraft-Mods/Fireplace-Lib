package dev.the_fireplace.lib.io.access;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.the_fireplace.lib.api.io.interfaces.access.SimpleBuffer;

import java.util.*;

public final class SimpleJsonBuffer implements SimpleBuffer {

    private final JsonObject jsonObject;

    public SimpleJsonBuffer(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public UUID readUUID(String key) {
        String uuidString = jsonObject.get(key).getAsString();
        try {
            return UUID.fromString(uuidString);
        } catch (IllegalArgumentException e) {
            throw new ClassCastException("String could not be converted to UUID!");
        }
    }

    @Override
    public String readString(String key) {
        return jsonObject.get(key).getAsString();
    }

    @Override
    public long readLong(String key) {
        return jsonObject.get(key).getAsLong();
    }

    @Override
    public int readInt(String key) {
        return jsonObject.get(key).getAsInt();
    }

    @Override
    public short readShort(String key) {
        return jsonObject.get(key).getAsShort();
    }

    @Override
    public byte readByte(String key) {
        return jsonObject.get(key).getAsByte();
    }

    @Override
    public double readDouble(String key) {
        return jsonObject.get(key).getAsDouble();
    }

    @Override
    public float readFloat(String key) {
        return jsonObject.get(key).getAsFloat();
    }

    @Override
    public boolean readBool(String key) {
        return jsonObject.get(key).getAsBoolean();
    }

    @Override
    public List<Boolean> readBoolList(String key) {
        List<Boolean> list = new ArrayList<>();
        JsonArray array = jsonObject.getAsJsonArray(key);
        for (JsonElement element : array) {
            list.add(element.getAsBoolean());
        }

        return list;
    }

    @Override
    public List<Number> readNumberList(String key) {
        List<Number> list = new ArrayList<>();
        JsonArray array = jsonObject.getAsJsonArray(key);
        for (JsonElement element : array) {
            list.add(element.getAsNumber());
        }

        return list;
    }

    @Override
    public List<String> readStringList(String key) {
        List<String> list = new ArrayList<>();
        JsonArray array = jsonObject.getAsJsonArray(key);
        for (JsonElement element : array) {
            list.add(element.getAsString());
        }

        return list;
    }

    @Override
    public Map<String, String> readStringToStringMap(String key) {
        Map<String, String> map = new HashMap<>();
        JsonArray array = jsonObject.getAsJsonArray(key);
        for (JsonElement element : array) {
            JsonObject object = element.getAsJsonObject();
            map.put(object.get("key").getAsString(), object.get("value").getAsString());
        }

        return map;
    }

    @Override
    public boolean hasKey(String key) {
        return jsonObject.has(key);
    }
}
