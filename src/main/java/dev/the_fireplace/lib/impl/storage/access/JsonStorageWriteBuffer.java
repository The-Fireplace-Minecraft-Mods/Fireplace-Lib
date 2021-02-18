package dev.the_fireplace.lib.impl.storage.access;

import com.google.gson.JsonObject;
import dev.the_fireplace.lib.api.storage.access.intermediary.StorageWriteBuffer;

import java.util.UUID;

public final class JsonStorageWriteBuffer implements StorageWriteBuffer {
    private final JsonObject obj;
    JsonStorageWriteBuffer(JsonObject obj) {
        this.obj = obj;
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
}
