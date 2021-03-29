package dev.the_fireplace.lib.impl.storage.access;

import com.google.gson.JsonObject;
import dev.the_fireplace.lib.api.storage.access.intermediary.StorageReadBuffer;
import dev.the_fireplace.lib.impl.FireplaceLib;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class JsonStorageReadBuffer implements StorageReadBuffer {
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
                FireplaceLib.getLogger().warn("Expected UUID for key '{}', got '{}'. Using default data instead.", key, uuidString);
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
    public Collection<String> getKeys() {
        return obj.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toSet());
    }
}
