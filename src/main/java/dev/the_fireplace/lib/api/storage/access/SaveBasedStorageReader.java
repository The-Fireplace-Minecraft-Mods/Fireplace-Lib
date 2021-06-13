package dev.the_fireplace.lib.api.storage.access;

import dev.the_fireplace.lib.api.storage.SaveBasedSerializable;

import java.util.Iterator;

public interface SaveBasedStorageReader {
    void readTo(SaveBasedSerializable readable);

    Iterator<String> getStoredIdsIterator(String database, String table);

    boolean isStored(String database, String table, String id);
    boolean isStored(SaveBasedSerializable saveBasedSerializable);
}
