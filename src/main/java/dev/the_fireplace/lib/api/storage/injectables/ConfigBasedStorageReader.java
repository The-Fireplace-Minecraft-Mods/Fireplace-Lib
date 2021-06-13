package dev.the_fireplace.lib.api.storage.injectables;

import dev.the_fireplace.lib.api.storage.interfaces.ConfigBasedSerializable;

public interface ConfigBasedStorageReader {
    void readTo(ConfigBasedSerializable readable);
}
