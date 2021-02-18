package dev.the_fireplace.lib.api.storage;

import dev.the_fireplace.lib.api.storage.access.ConfigBasedStorageReader;
import dev.the_fireplace.lib.api.storage.access.ConfigBasedStorageWriter;
import dev.the_fireplace.lib.api.storage.utility.ReloadableManager;

public abstract class LazyConfig implements ConfigBasedSerializable, Reloadable {
    private final ConfigBasedStorageReader configBasedStorageReader = ConfigBasedStorageReader.getInstance();
    private final ConfigBasedStorageWriter configBasedStorageWriter = ConfigBasedStorageWriter.getInstance();
    protected final ReloadableManager reloadableManager = ReloadableManager.getInstance();

    protected LazyConfig() {
        load();
        save();
        reloadableManager.register(this);
    }

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
