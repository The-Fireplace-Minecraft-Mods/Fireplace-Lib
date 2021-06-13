package dev.the_fireplace.lib.api.storage.injectables;

import dev.the_fireplace.lib.api.storage.interfaces.ConfigBasedSerializable;

public interface ConfigBasedStorageWriter {
    boolean write(ConfigBasedSerializable writable);
    boolean delete(ConfigBasedSerializable writable);
}
