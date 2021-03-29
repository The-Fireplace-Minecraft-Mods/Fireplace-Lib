package dev.the_fireplace.lib.impl.io;

import dev.the_fireplace.lib.api.io.FilePathStorage;
import dev.the_fireplace.lib.api.storage.access.intermediary.StorageReadBuffer;
import dev.the_fireplace.lib.api.storage.access.intermediary.StorageWriteBuffer;
import dev.the_fireplace.lib.api.storage.lazy.Defaultable;
import dev.the_fireplace.lib.api.storage.lazy.LazyConfig;
import dev.the_fireplace.lib.api.storage.lazy.LazyConfigInitializer;
import dev.the_fireplace.lib.impl.FireplaceLib;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class LazyFilePathMemory extends LazyConfig implements Defaultable {
    private static final LazyFilePathMemory INSTANCE = LazyConfigInitializer.lazyInitialize(new LazyFilePathMemory());
    @Deprecated
    public static final FilePathStorage ACCESS = INSTANCE.getAccess();
    private LazyFilePathMemory() {}

    private final Access access = new Access();

    private Access getAccess() {
        return access;
    }

    private final Map<String, String> lazyMemory = new ConcurrentHashMap<>(1);
    @Override
    public String getSubfolderName() {
        return FireplaceLib.MODID;
    }

    @Override
    public String getId() {
        return "paths";
    }

    @Override
    public void readFrom(StorageReadBuffer buffer) {
        for (String key: buffer.getKeys()) {
            lazyMemory.put(key, buffer.readString(key, ""));
        }
    }

    @Override
    public void writeTo(StorageWriteBuffer buffer) {
        for (Map.Entry<String, String> entry: lazyMemory.entrySet()) {
            buffer.writeString(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public boolean isDefault() {
        return lazyMemory.isEmpty() || lazyMemory.values().stream().allMatch(String::isEmpty);
    }

    public class Access implements FilePathStorage {
        @Override
        @Nullable
        public String getFilePath(String key) {
            return LazyFilePathMemory.this.lazyMemory.get(key);
        }

        @Override
        public void storeFilePath(String key, String path) {
            LazyFilePathMemory.this.lazyMemory.put(key, path);
            LazyFilePathMemory.this.save();
        }
    }
}
