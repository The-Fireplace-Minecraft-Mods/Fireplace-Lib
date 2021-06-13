package dev.the_fireplace.lib.api.storage.injectables;

import dev.the_fireplace.lib.api.storage.interfaces.SaveBasedSerializable;

public interface SaveBasedStorageWriter {
    boolean write(SaveBasedSerializable writable);
    boolean delete(SaveBasedSerializable writable);
}
