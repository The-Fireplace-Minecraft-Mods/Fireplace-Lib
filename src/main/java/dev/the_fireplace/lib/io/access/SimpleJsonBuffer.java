package dev.the_fireplace.lib.io.access;

import com.google.gson.JsonObject;
import dev.the_fireplace.lib.api.io.interfaces.access.SimpleBuffer;

import java.util.UUID;

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
    public boolean hasKey(String key) {
        return jsonObject.has(key);
    }
}
