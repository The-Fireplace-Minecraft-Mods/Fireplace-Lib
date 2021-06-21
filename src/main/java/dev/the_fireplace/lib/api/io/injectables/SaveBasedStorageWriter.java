package dev.the_fireplace.lib.api.io.injectables;

import dev.the_fireplace.lib.api.io.interfaces.SaveBasedSerializable;

public interface SaveBasedStorageWriter {
    boolean write(SaveBasedSerializable writable);
    boolean delete(SaveBasedSerializable writable);
}
