package dev.the_fireplace.lib.api.storage;

import dev.the_fireplace.lib.api.storage.access.intermediary.StorageReadBuffer;

public interface Readable {
    void readFrom(StorageReadBuffer buffer);
}
