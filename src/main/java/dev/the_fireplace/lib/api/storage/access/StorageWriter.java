package dev.the_fireplace.lib.api.storage.access;

import dev.the_fireplace.lib.api.storage.Writable;
import dev.the_fireplace.lib.impl.storage.JsonStorageWriter;

public interface StorageWriter {
    static StorageWriter getInstance() {
        //noinspection deprecation
        return JsonStorageWriter.INSTANCE;
    }
    boolean write(Writable writable);
}
