package dev.the_fireplace.lib.impl.lazyio;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.io.injectables.ConfigBasedStorageReader;
import dev.the_fireplace.lib.api.io.injectables.ConfigBasedStorageWriter;
import dev.the_fireplace.lib.api.lazyio.injectables.ConfigStateManager;
import dev.the_fireplace.lib.api.lazyio.interfaces.Config;

import javax.inject.Inject;
import javax.inject.Singleton;

@Implementation
@Singleton
public final class ConfigStateManagerImpl implements ConfigStateManager {

    private final ConfigBasedStorageReader storageReader;
    private final ConfigBasedStorageWriter storageWriter;

    @Inject
    public ConfigStateManagerImpl(ConfigBasedStorageReader storageReader, ConfigBasedStorageWriter storageWriter) {
        this.storageReader = storageReader;
        this.storageWriter = storageWriter;
    }

    @Override
    public <T extends Config> T initialize(T config) {
        load(config);
        save(config);

        return config;
    }

    @Override
    public void load(Config config) {
        storageReader.readTo(config);
    }

    @Override
    public void reload(Config config) {
        load(config);
    }

    @Override
    public void save(Config config) {
        storageWriter.write(config);
    }
}
