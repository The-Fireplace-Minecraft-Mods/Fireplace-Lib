package dev.the_fireplace.lib.api.storage.interfaces;

import dev.the_fireplace.lib.api.storage.interfaces.access.StorageReadBuffer;

public interface Readable {
    void readFrom(StorageReadBuffer buffer);
}
