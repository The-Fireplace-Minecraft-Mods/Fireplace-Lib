package dev.the_fireplace.lib.api.io.injectables;

import dev.the_fireplace.lib.api.io.interfaces.SaveBasedSerializable;

import java.util.Iterator;

public interface SaveBasedStorageReader
{
    void readTo(SaveBasedSerializable readable);

    Iterator<String> getStoredIdsIterator(String database, String table);

    boolean isStored(String database, String table, String id);

    boolean isStored(SaveBasedSerializable saveBasedSerializable);
}
