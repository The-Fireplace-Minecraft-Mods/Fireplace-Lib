package dev.the_fireplace.lib.api.io.interfaces;

import dev.the_fireplace.lib.api.io.interfaces.access.StorageWriteBuffer;

public interface Writable
{
    void writeTo(StorageWriteBuffer buffer);
}
