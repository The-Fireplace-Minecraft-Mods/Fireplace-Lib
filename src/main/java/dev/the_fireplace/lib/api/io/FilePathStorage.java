package dev.the_fireplace.lib.api.io;

import dev.the_fireplace.lib.impl.io.LazyFilePathMemory;

import javax.annotation.Nullable;

public interface FilePathStorage {
    static FilePathStorage getInstance() {
        //noinspection deprecation
        return LazyFilePathMemory.ACCESS;
    }
    @Nullable
    String getFilePath(String key);

    void storeFilePath(String key, String path);
}
