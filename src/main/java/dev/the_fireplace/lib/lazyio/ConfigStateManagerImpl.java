package dev.the_fireplace.lib.lazyio;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.io.injectables.ConfigBasedStorageReader;
import dev.the_fireplace.lib.api.io.injectables.ConfigBasedStorageWriter;
import dev.the_fireplace.lib.api.lazyio.injectables.ConfigStateManager;
import dev.the_fireplace.lib.api.lazyio.injectables.ReloadableManager;
import dev.the_fireplace.lib.api.lazyio.interfaces.Config;
import dev.the_fireplace.lib.api.lazyio.interfaces.Reloadable;
import dev.the_fireplace.lib.entrypoints.FireplaceLib;
import dev.the_fireplace.lib.io.access.SchemaValidator;

import javax.inject.Inject;
import javax.inject.Singleton;

@Implementation
@Singleton
public final class ConfigStateManagerImpl implements ConfigStateManager {

    private final ConfigBasedStorageReader storageReader;
    private final ConfigBasedStorageWriter storageWriter;
    private final ReloadableManager reloadableManager;

    @Inject
    public ConfigStateManagerImpl(ConfigBasedStorageReader storageReader, ConfigBasedStorageWriter storageWriter, ReloadableManager reloadableManager) {
        this.storageReader = storageReader;
        this.storageWriter = storageWriter;
        this.reloadableManager = reloadableManager;
    }

    @Override
    public <T extends Config> T initialize(T config) {
        load(config);
        save(config);
        registerReloadable(config);

        return config;
    }

    private <T extends Config> void registerReloadable(T config) {
        StringBuilder reloadGroup = new StringBuilder();
        String subFolder = SchemaValidator.isValid(config.getSubfolderName()) ? SchemaValidator.minimizeSchema(config.getSubfolderName()) : "";
        if (!subFolder.isEmpty()) {
            reloadGroup.append(subFolder).append("/");
        }
        String configId = SchemaValidator.isValid(config.getId()) ? SchemaValidator.minimizeSchema(config.getId()) : "";
        if (!configId.isEmpty()) {
            reloadGroup.append(configId);
        }

        if (!reloadGroup.toString().isEmpty()) {
            reloadableManager.register(new Reloadable() {
                @Override
                public void reload() {
                    ConfigStateManagerImpl.this.reload(config);
                }

                @Override
                public String getReloadGroup() {
                    return reloadGroup.toString();
                }
            });
        } else {
            FireplaceLib.getLogger().warn("Config was registered without ID or Subfolder, unable to register reloadable!", new Exception("Stacktrace"));
        }
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
