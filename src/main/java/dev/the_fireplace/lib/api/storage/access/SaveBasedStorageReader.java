package dev.the_fireplace.lib.api.storage.access;

import dev.the_fireplace.lib.api.storage.SaveBasedSerializable;
import dev.the_fireplace.lib.impl.storage.access.SaveBasedJsonStorageReader;

public interface SaveBasedStorageReader {
    static SaveBasedStorageReader getInstance() {
        //noinspection deprecation
        return SaveBasedJsonStorageReader.INSTANCE;
    }
    void readTo(SaveBasedSerializable readable);
}
