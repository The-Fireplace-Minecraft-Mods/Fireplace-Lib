package dev.the_fireplace.lib.api.storage.access;

import dev.the_fireplace.lib.api.storage.ConfigBasedSerializable;

public interface ConfigBasedStorageReader {
    void readTo(ConfigBasedSerializable readable);
}
