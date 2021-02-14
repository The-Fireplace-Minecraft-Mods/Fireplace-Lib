package dev.the_fireplace.lib.api.storage;

import dev.the_fireplace.lib.api.storage.access.intermediary.StorageType;

public interface Storable {
    String getDatabase();
    String getTable();
    String getId();
    StorageType getType();
}
