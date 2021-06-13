package dev.the_fireplace.lib.api.storage.lazy;

import dev.the_fireplace.annotateddi.AnnotatedDI;
import dev.the_fireplace.lib.api.storage.ConfigBasedSerializable;
import dev.the_fireplace.lib.api.storage.Reloadable;
import dev.the_fireplace.lib.api.storage.access.ConfigBasedStorageReader;
import dev.the_fireplace.lib.api.storage.access.ConfigBasedStorageWriter;

public abstract class LazyConfig implements ConfigBasedSerializable, Reloadable {
    private final ConfigBasedStorageReader configBasedStorageReader = AnnotatedDI.getInjector().getInstance(ConfigBasedStorageReader.class);
    private final ConfigBasedStorageWriter configBasedStorageWriter = AnnotatedDI.getInjector().getInstance(ConfigBasedStorageWriter.class);

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
