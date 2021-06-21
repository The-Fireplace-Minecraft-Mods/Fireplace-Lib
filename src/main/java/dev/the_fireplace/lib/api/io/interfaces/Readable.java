package dev.the_fireplace.lib.api.io.interfaces;

import dev.the_fireplace.lib.api.io.interfaces.access.StorageReadBuffer;

public interface Readable {
    void readFrom(StorageReadBuffer buffer);
}
