package dev.the_fireplace.lib.api.storage.access;

import dev.the_fireplace.lib.api.storage.ConfigBasedSerializable;

public interface ConfigBasedStorageWriter {
    boolean write(ConfigBasedSerializable writable);
    boolean delete(ConfigBasedSerializable writable);
}
