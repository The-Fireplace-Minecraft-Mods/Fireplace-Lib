package dev.the_fireplace.lib.api.io.injectables;

import dev.the_fireplace.lib.api.io.interfaces.ConfigBasedSerializable;

public interface ConfigBasedStorageWriter {
    boolean write(ConfigBasedSerializable writable);
    boolean delete(ConfigBasedSerializable writable);
}
