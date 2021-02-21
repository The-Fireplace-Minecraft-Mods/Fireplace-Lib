package dev.the_fireplace.lib.api.storage.lazy;

import dev.the_fireplace.lib.api.storage.ConfigBasedSerializable;
import dev.the_fireplace.lib.api.storage.Reloadable;
import dev.the_fireplace.lib.api.storage.access.ConfigBasedStorageReader;
import dev.the_fireplace.lib.api.storage.access.ConfigBasedStorageWriter;

public abstract class LazyConfig implements ConfigBasedSerializable, Reloadable {
    private final ConfigBasedStorageReader configBasedStorageReader = ConfigBasedStorageReader.getInstance();
    private final ConfigBasedStorageWriter configBasedStorageWriter = ConfigBasedStorageWriter.getInstance();

    protected void load() {
        configBasedStorageReader.readTo(this);
    }

    protected void save() {
        configBasedStorageWriter.write(this);
    }

    @Override
    public void reload() {
        load();
    }

    @Override
    public String getReloadGroup() {
        return getId();
    }
}
