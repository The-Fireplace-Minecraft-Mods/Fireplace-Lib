package dev.the_fireplace.lib.api.storage.access;

import dev.the_fireplace.lib.api.storage.SaveBasedSerializable;

public interface SaveBasedStorageWriter {
    boolean write(SaveBasedSerializable writable);
    boolean delete(SaveBasedSerializable writable);
}
