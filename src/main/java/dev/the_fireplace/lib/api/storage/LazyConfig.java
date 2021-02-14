package dev.the_fireplace.lib.api.storage;

import dev.the_fireplace.lib.api.storage.access.ConfigBasedStorageReader;
import dev.the_fireplace.lib.api.storage.access.ConfigBasedStorageWriter;

public abstract class LazyConfig implements ConfigBasedSerializable {
    private final ConfigBasedStorageReader configBasedStorageReader = ConfigBasedStorageReader.getInstance();
    private final ConfigBasedStorageWriter configBasedStorageWriter = ConfigBasedStorageWriter.getInstance();
    protected LazyConfig() {
        load();
        save();
    }

    protected void load() {
        configBasedStorageReader.readTo(this);
    }

    protected void save() {
        configBasedStorageWriter.write(this);
    }
}
