package dev.the_fireplace.lib.lazyio;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.io.injectables.ConfigBasedStorageReader;
import dev.the_fireplace.lib.api.io.injectables.ConfigBasedStorageWriter;
import dev.the_fireplace.lib.api.io.interfaces.access.SimpleBuffer;
import dev.the_fireplace.lib.api.lazyio.injectables.ConfigStateManager;
import dev.the_fireplace.lib.api.lazyio.injectables.ReloadableManager;
import dev.the_fireplace.lib.api.lazyio.interfaces.Config;
import dev.the_fireplace.lib.api.lazyio.interfaces.Reloadable;
import dev.the_fireplace.lib.domain.io.JsonBufferDiffGenerator;
import dev.the_fireplace.lib.entrypoints.FireplaceLib;
import dev.the_fireplace.lib.io.access.JsonStorageWriteBuffer;
import dev.the_fireplace.lib.io.access.SchemaValidator;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Implementation
@Singleton
public final class ConfigStateManagerImpl implements ConfigStateManager
{
    private final ConfigBasedStorageReader storageReader;
    private final ConfigBasedStorageWriter storageWriter;
    private final ReloadableManager reloadableManager;
    private final JsonBufferDiffGenerator bufferDiffGenerator;

    private final ConcurrentMap<Config, Reloadable> configReloadables;

    @Inject
    public ConfigStateManagerImpl(
        ConfigBasedStorageReader storageReader,
        ConfigBasedStorageWriter storageWriter,
        ReloadableManager reloadableManager,
        JsonBufferDiffGenerator bufferDiffGenerator
    ) {
        this.storageReader = storageReader;
        this.storageWriter = storageWriter;
        this.reloadableManager = reloadableManager;
        this.bufferDiffGenerator = bufferDiffGenerator;
        configReloadables = new ConcurrentHashMap<>();
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
            Reloadable reloadable = new Reloadable()
            {
                @Override
                public void reload() {
                    JsonStorageWriteBuffer previousState = new JsonStorageWriteBuffer();
                    config.writeTo(previousState);

                    ConfigStateManagerImpl.this.reload(config);

                    JsonStorageWriteBuffer currentState = new JsonStorageWriteBuffer();
                    config.writeTo(currentState);

                    SimpleBuffer diffBuffer = bufferDiffGenerator.generateLeftDiff(previousState, currentState);
                    config.afterReload(diffBuffer);
                }

                @Override
                public String getReloadGroup() {
                    return reloadGroup.toString();
                }
            };
            configReloadables.put(config, reloadable);
            reloadableManager.register(reloadable);
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

    @Override
    public void delete(Config config) {
        tearDown(config);
        storageWriter.delete(config);
    }

    private void tearDown(Config config) {
        Reloadable reloadable = configReloadables.remove(config);
        if (reloadable != null) {
            reloadableManager.unregister(reloadable);
        }
    }
}
