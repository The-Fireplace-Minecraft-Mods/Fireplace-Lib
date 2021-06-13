package dev.the_fireplace.lib.api.storage.interfaces;

import dev.the_fireplace.lib.api.storage.interfaces.access.StorageWriteBuffer;

public interface Writable {
    void writeTo(StorageWriteBuffer buffer);
}
