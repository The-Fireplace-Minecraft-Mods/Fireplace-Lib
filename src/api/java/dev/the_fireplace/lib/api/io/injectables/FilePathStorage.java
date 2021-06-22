package dev.the_fireplace.lib.api.io.injectables;

import javax.annotation.Nullable;

public interface FilePathStorage {
    @Nullable
    String getFilePath(String key);

    void storeFilePath(String key, String path);
}
