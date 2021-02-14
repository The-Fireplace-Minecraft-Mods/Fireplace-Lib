package dev.the_fireplace.lib.api.storage;

public interface SaveBasedSerializable extends Readable, Writable {
    String getDatabase();
    String getTable();
    String getId();
}
