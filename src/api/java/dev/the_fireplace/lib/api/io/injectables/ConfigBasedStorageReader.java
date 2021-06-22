package dev.the_fireplace.lib.api.io.injectables;

import dev.the_fireplace.lib.api.io.interfaces.ConfigBasedSerializable;

public interface ConfigBasedStorageReader {
    void readTo(ConfigBasedSerializable readable);
}
