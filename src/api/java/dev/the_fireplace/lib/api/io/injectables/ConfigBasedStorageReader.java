package dev.the_fireplace.lib.api.io.injectables;

import dev.the_fireplace.lib.api.io.interfaces.ConfigBasedSerializable;

import java.util.Iterator;

public interface ConfigBasedStorageReader
{
    void readTo(ConfigBasedSerializable readable);

    Iterator<String> getStoredConfigs(String subfolder);
}
