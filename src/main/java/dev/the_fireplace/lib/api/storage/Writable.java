package dev.the_fireplace.lib.api.storage;

import dev.the_fireplace.lib.api.storage.access.intermediary.StorageWriteBuffer;

public interface Writable {
    void writeTo(StorageWriteBuffer buffer);
}
