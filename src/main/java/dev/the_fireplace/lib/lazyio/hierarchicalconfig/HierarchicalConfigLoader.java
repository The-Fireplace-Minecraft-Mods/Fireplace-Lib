package dev.the_fireplace.lib.lazyio.hierarchicalconfig;

import dev.the_fireplace.lib.api.io.interfaces.access.SimpleBuffer;
import dev.the_fireplace.lib.api.lazyio.injectables.ReloadableManager;
import dev.the_fireplace.lib.api.lazyio.interfaces.HierarchicalConfig;
import dev.the_fireplace.lib.api.lazyio.interfaces.Reloadable;
import dev.the_fireplace.lib.domain.io.HierarchicalConfigReader;
import dev.the_fireplace.lib.domain.io.HierarchicalConfigWriter;
import dev.the_fireplace.lib.domain.io.JsonBufferDiffGenerator;
import dev.the_fireplace.lib.entrypoints.FireplaceLib;
import dev.the_fireplace.lib.io.access.JsonStorageWriteBuffer;
import dev.the_fireplace.lib.io.access.SchemaValidator;
import net.minecraft.util.Identifier;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public final class HierarchicalConfigLoader
{

    private final HierarchicalConfigReader storageReader;
    private final HierarchicalConfigWriter storageWriter;
    private final ReloadableManager reloadableManager;
    private final JsonBufferDiffGenerator bufferDiffGenerator;

    private final Map<String, Reloadable> configReloaders;

    @Inject
    public HierarchicalConfigLoader(
        HierarchicalConfigReader storageReader,
        HierarchicalConfigWriter storageWriter,
        ReloadableManager reloadableManager,
        JsonBufferDiffGenerator bufferDiffGenerator
    ) {
        this.storageReader = storageReader;
        this.storageWriter = storageWriter;
        this.reloadableManager = reloadableManager;
        this.bufferDiffGenerator = bufferDiffGenerator;

        this.configReloaders = new ConcurrentHashMap<>();
    }

    public <T extends HierarchicalConfig> T initialize(T config, String domain, String id) {
        load(config, domain, id);
        save(config, domain, id);
        registerReloadable(config, domain, id);

        return config;
    }

    public <T extends HierarchicalConfig> T initialize(T config, String domain, Identifier id) {
        load(config, domain, id);
        save(config, domain, id);
        registerReloadable(config, domain, id);

        return config;
    }

    private void registerReloadable(HierarchicalConfig config, String domain, String id) {
        String reloadId = getReloadId(domain, id);

        if (reloadId.isEmpty()) {
            FireplaceLib.getLogger().warn("Hierarchical Config was registered without ID or Domain, unable to register reloadable!", new Exception("Stacktrace"));
            return;
        }
        Reloadable configReloader = buildConfigReloadable(config, domain, id, reloadId);

        registerReloader(reloadId, configReloader);
    }

    private void registerReloadable(HierarchicalConfig config, String domain, Identifier id) {
        String reloadId = getReloadId(domain, id);

        if (reloadId.isEmpty()) {
            FireplaceLib.getLogger().warn("Hierarchical Config was registered without ID or Domain, unable to register reloadable!", new Exception("Stacktrace"));
            return;
        }
        Reloadable configReloader = buildConfigReloadable(config, domain, id, reloadId);

        registerReloader(reloadId, configReloader);
    }

    private void registerReloader(String reloadId, Reloadable configReloader) {
        Reloadable oldReloader = configReloaders.get(reloadId);
        if (oldReloader != null) {
            reloadableManager.unregister(oldReloader);
        }

        configReloaders.put(reloadId, configReloader);
        reloadableManager.register(configReloader);
    }

    private Reloadable buildConfigReloadable(HierarchicalConfig config, String domain, String id, String reloadId) {
        return new Reloadable()
        {
            @Override
            public void reload() {
                JsonStorageWriteBuffer previousState = new JsonStorageWriteBuffer();
                config.writeTo(previousState);

                HierarchicalConfigLoader.this.reload(config, domain, id);

                JsonStorageWriteBuffer currentState = new JsonStorageWriteBuffer();
                config.writeTo(currentState);

                SimpleBuffer diffBuffer = bufferDiffGenerator.generateLeftDiff(previousState, currentState);
                config.afterReload(diffBuffer);
            }

            @Override
            public String getReloadGroup() {
                return !domain.isEmpty() ? domain : reloadId;
            }
        };
    }

    private Reloadable buildConfigReloadable(HierarchicalConfig config, String domain, Identifier id, String reloadId) {
        return new Reloadable()
        {
            @Override
            public void reload() {
                JsonStorageWriteBuffer previousState = new JsonStorageWriteBuffer();
                config.writeTo(previousState);

                HierarchicalConfigLoader.this.reload(config, domain, id);

                JsonStorageWriteBuffer currentState = new JsonStorageWriteBuffer();
                config.writeTo(currentState);

                SimpleBuffer diffBuffer = bufferDiffGenerator.generateLeftDiff(previousState, currentState);
                config.afterReload(diffBuffer);
            }

            @Override
            public String getReloadGroup() {
                return !domain.isEmpty() ? domain : reloadId;
            }
        };
    }

    private String getReloadId(String domain, String id) {
        StringBuilder reloadId = new StringBuilder();
        String subFolder = validateConfigName(domain);
        if (!subFolder.isEmpty()) {
            reloadId.append(subFolder).append("/");
        }
        String configId = validateConfigName(id);
        if (!configId.isEmpty()) {
            reloadId.append(configId);
        }

        return reloadId.toString();
    }

    private String getReloadId(String domain, Identifier id) {
        StringBuilder reloadId = new StringBuilder();
        String subFolder = validateConfigName(domain);
        if (!subFolder.isEmpty()) {
            reloadId.append(subFolder).append("/");
        }
        String configNamespace = validateConfigName(id.getNamespace());
        if (!configNamespace.isEmpty()) {
            reloadId.append(configNamespace).append(":");
        }
        String configId = validateConfigName(id.getPath());
        if (!configId.isEmpty()) {
            reloadId.append(configId);
        }

        return reloadId.toString();
    }

    private String validateConfigName(String domain) {
        return SchemaValidator.isValid(domain) ? SchemaValidator.minimizeSchema(domain) : "";
    }

    public void load(HierarchicalConfig config, String domain, String id) {
        storageReader.readTo(config, domain, id);
    }

    public void load(HierarchicalConfig config, String domain, Identifier id) {
        storageReader.readTo(config, domain, id);
    }

    public void save(HierarchicalConfig config, String domain, String id) {
        storageWriter.write(config, domain, id);
    }

    public void save(HierarchicalConfig config, String domain, Identifier id) {
        storageWriter.write(config, domain, id);
    }

    private void reload(HierarchicalConfig config, String domain, String id) {
        load(config, domain, id);
    }

    private void reload(HierarchicalConfig config, String domain, Identifier id) {
        load(config, domain, id);
    }

    public boolean delete(String domain, String id) {
        boolean deleted = storageWriter.delete(domain, id);
        if (deleted) {
            String reloadId = getReloadId(domain, id);
            configReloaders.remove(reloadId);
        }

        return deleted;
    }

    public boolean delete(String domain, Identifier id) {
        boolean deleted = storageWriter.delete(domain, id);
        if (deleted) {
            String reloadId = getReloadId(domain, id);
            configReloaders.remove(reloadId);
        }

        return deleted;
    }
}
