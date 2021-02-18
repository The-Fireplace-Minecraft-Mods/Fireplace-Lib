package dev.the_fireplace.lib.api.storage.access;

import dev.the_fireplace.lib.api.storage.ConfigBasedSerializable;
import dev.the_fireplace.lib.impl.storage.access.ConfigBasedJsonStorageReader;

public interface ConfigBasedStorageReader {
    static ConfigBasedStorageReader getInstance() {
        //noinspection deprecation
        return ConfigBasedJsonStorageReader.INSTANCE;
    }
    void readTo(ConfigBasedSerializable readable);
}
