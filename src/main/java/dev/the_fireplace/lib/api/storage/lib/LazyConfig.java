package dev.the_fireplace.lib.api.storage.lib;

import dev.the_fireplace.annotateddi.api.DIContainer;
import dev.the_fireplace.lib.api.storage.injectables.ConfigBasedStorageReader;
import dev.the_fireplace.lib.api.storage.injectables.ConfigBasedStorageWriter;
import dev.the_fireplace.lib.api.storage.interfaces.ConfigBasedSerializable;
import dev.the_fireplace.lib.api.storage.interfaces.Reloadable;

public abstract class LazyConfig implements ConfigBasedSerializable, Reloadable {
    private final ConfigBasedStorageReader configBasedStorageReader = DIContainer.get().getInstance(ConfigBasedStorageReader.class);
    private final ConfigBasedStorageWriter configBasedStorageWriter = DIContainer.get().getInstance(ConfigBasedStorageWriter.class);

    protected void load() {
        configBasedStorageReader.readTo(this);
    }

    public void save() {
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
