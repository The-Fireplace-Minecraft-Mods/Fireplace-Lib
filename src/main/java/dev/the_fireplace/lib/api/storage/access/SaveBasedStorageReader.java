package dev.the_fireplace.lib.api.storage.access;

import dev.the_fireplace.lib.api.storage.SaveBasedSerializable;
import dev.the_fireplace.lib.impl.storage.access.SaveBasedJsonStorageReader;

import java.util.Iterator;

public interface SaveBasedStorageReader {
    static SaveBasedStorageReader getInstance() {
        //noinspection deprecation
        return SaveBasedJsonStorageReader.INSTANCE;
    }
    void readTo(SaveBasedSerializable readable);

    Iterator<String> getStoredIdsIterator(String database, String table);

    boolean isStored(String database, String table, String id);
    boolean isStored(SaveBasedSerializable saveBasedSerializable);
}
