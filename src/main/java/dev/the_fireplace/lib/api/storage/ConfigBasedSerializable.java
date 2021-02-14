package dev.the_fireplace.lib.api.storage;

public interface ConfigBasedSerializable extends Readable, Writable {

    default String getSubfolderName() {
        return "";
    }
    String getId();
}
