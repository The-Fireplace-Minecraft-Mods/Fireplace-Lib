package dev.the_fireplace.lib.api.storage.access;

import dev.the_fireplace.lib.api.storage.Readable;
import dev.the_fireplace.lib.impl.storage.JsonStorageReader;

public interface StorageReader {
    static StorageReader getInstance() {
        //noinspection deprecation
        return JsonStorageReader.INSTANCE;
    }
    void readTo(Readable readable);
}
