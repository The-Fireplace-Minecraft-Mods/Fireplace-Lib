package dev.the_fireplace.lib.api.storage.access;

import dev.the_fireplace.lib.api.storage.ConfigBasedSerializable;
import dev.the_fireplace.lib.impl.storage.ConfigBasedJsonStorageWriter;

public interface ConfigBasedStorageWriter {
    static ConfigBasedStorageWriter getInstance() {
        //noinspection deprecation
        return ConfigBasedJsonStorageWriter.INSTANCE;
    }
    boolean write(ConfigBasedSerializable writable);
}
