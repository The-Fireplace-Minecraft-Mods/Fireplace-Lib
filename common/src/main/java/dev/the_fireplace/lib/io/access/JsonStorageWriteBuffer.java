package dev.the_fireplace.lib.io.access;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.the_fireplace.lib.api.io.interfaces.access.StorageWriteBuffer;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class JsonStorageWriteBuffer implements StorageWriteBuffer
{
    private final JsonObject obj;

    public JsonStorageWriteBuffer() {
        this.obj = new JsonObject();
    }

    JsonObject getObj() {
        return obj;
    }

    @Override
    public void writeUUID(String key, UUID value) {
        obj.addProperty(key, value.toString());
    }

    @Override
    public void writeString(String key, String value) {
        obj.addProperty(key, value);
    }

    @Override
    public void writeLong(String key, long value) {
        obj.addProperty(key, value);
    }

    @Override
    public void writeInt(String key, int value) {
        obj.addProperty(key, value);
    }

    @Override
    public void writeShort(String key, short value) {
        obj.addProperty(key, value);
    }

    @Override
    public void writeByte(String key, byte value) {
        obj.addProperty(key, value);
    }

    @Override
    public void writeDouble(String key, double value) {
        obj.addProperty(key, value);
    }

    @Override
    public void writeFloat(String key, float value) {
        obj.addProperty(key, value);
    }

    @Override
    public void writeBool(String key, boolean value) {
        obj.addProperty(key, value);
    }

    @Override
    public void writeBoolList(String key, List<Boolean> values) {
        JsonArray jsonArray = new JsonArray();
        for (Boolean value : values) {
            jsonArray.add(value);
        }
        obj.add(key, jsonArray);
    }

    @Override
    public void writeNumberList(String key, List<Number> values) {
        JsonArray jsonArray = new JsonArray();
        for (Number value : values) {
            jsonArray.add(value);
        }
        obj.add(key, jsonArray);
    }

    @Override
    public void writeStringList(String key, List<String> values) {
        JsonArray jsonArray = new JsonArray();
        for (String value : values) {
            jsonArray.add(value);
        }
        obj.add(key, jsonArray);
    }

    @Override
    public void writeStringToStringMap(String key, Map<String, String> values) {
        JsonArray jsonArray = new JsonArray();
        for (Map.Entry<String, String> entry : values.entrySet()) {
            JsonObject object = new JsonObject();
            object.addProperty("key", entry.getKey());
            object.addProperty("value", entry.getValue());
            jsonArray.add(object);
        }
        obj.add(key, jsonArray);
    }
}
