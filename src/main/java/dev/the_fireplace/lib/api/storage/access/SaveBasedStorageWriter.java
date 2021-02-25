package dev.the_fireplace.lib.api.storage.access;

import dev.the_fireplace.lib.api.storage.SaveBasedSerializable;
import dev.the_fireplace.lib.impl.storage.access.SaveBasedJsonStorageWriter;

public interface SaveBasedStorageWriter {
    static SaveBasedStorageWriter getInstance() {
        //noinspection deprecation
        return SaveBasedJsonStorageWriter.INSTANCE;
    }
    boolean write(SaveBasedSerializable writable);
    boolean delete(SaveBasedSerializable writable);
}
