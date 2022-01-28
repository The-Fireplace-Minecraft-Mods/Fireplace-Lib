package dev.the_fireplace.lib.lazyio.hierarchicalconfig;

import com.google.common.collect.Sets;
import dev.the_fireplace.lib.FireplaceLibConstants;
import dev.the_fireplace.lib.api.lazyio.injectables.ReloadableManager;
import dev.the_fireplace.lib.api.lazyio.interfaces.HierarchicalConfig;
import dev.the_fireplace.lib.api.lazyio.interfaces.HierarchicalConfigManager;
import dev.the_fireplace.lib.api.lazyio.interfaces.Reloadable;
import dev.the_fireplace.lib.io.access.JsonStoragePath;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class HierarchicalConfigManagerImpl<T extends HierarchicalConfig> implements HierarchicalConfigManager<T>
{

    private final HierarchicalConfigLoader configLoader;
    private final JsonStoragePath jsonStoragePath;
    private final ReloadableManager reloadableManager;

    private final String domain;
    private final T defaultConfig;
    private final Iterable<String> allowedModuleIds;
    private final Map<String, T> modules;

    public HierarchicalConfigManagerImpl(
        String domain,
        T defaultConfig,
        Iterable<String> allowedModuleIds,
        HierarchicalConfigLoader configLoader,
        JsonStoragePath jsonStoragePath,
        ReloadableManager reloadableManager
    ) {
        this.domain = domain;
        this.defaultConfig = defaultConfig;
        this.allowedModuleIds = allowedModuleIds;
        this.modules = new ConcurrentHashMap<>();
        this.configLoader = configLoader;
        this.jsonStoragePath = jsonStoragePath;
        this.reloadableManager = reloadableManager;

        loadExistingHierarchy();
        registerHierarchyReloadable();
    }

    private void loadExistingHierarchy() {
        for (String id : allowedModuleIds) {
            Path filePath = jsonStoragePath.resolveConfigBasedJsonFilePath(domain, id);
            if (filePath.toFile().exists() && !modules.containsKey(id)) {
                HierarchicalConfig module = defaultConfig.clone();
                try {
                    //noinspection unchecked
                    addCustom(id, (T) module);
                } catch (ClassCastException exception) {
                    FireplaceLibConstants.getLogger().error("Cloned hierarchical config was not of the expected type!", exception);
                }
            }
        }
    }

    private void registerHierarchyReloadable() {
        reloadableManager.register(new Reloadable()
        {
            @Override
            public void reload() {
                loadExistingHierarchy();
            }

            @Override
            public String getReloadGroup() {
                return domain;
            }
        });
    }

    @Override
    public Iterable<String> getAllowedModuleIds() {
        return allowedModuleIds;
    }

    @Override
    public T get(String moduleId) {
        return modules.getOrDefault(moduleId, defaultConfig);
    }

    @Override
    public Collection<String> getCustoms() {
        return Sets.newHashSet(modules.keySet());
    }

    @Override
    public boolean isCustom(String moduleId) {
        return modules.containsKey(moduleId);
    }

    @Override
    public void addCustom(String moduleId, T module) {
        modules.put(moduleId, module);
        configLoader.initialize(module, domain, moduleId);
    }

    @Override
    public boolean deleteCustom(String moduleId) {
        T module = modules.remove(moduleId);
        if (module == null) {
            return false;
        }

        return configLoader.delete(domain, moduleId);
    }

    @Override
    public void saveAllCustoms() {
        for (Map.Entry<String, T> moduleEntry : modules.entrySet()) {
            configLoader.save(moduleEntry.getValue(), domain, moduleEntry.getKey());
        }
    }

    @Override
    public void saveCustom(String id) {
        T module = modules.get(id);
        if (module == null) {
            FireplaceLibConstants.getLogger().error("Custom config does not exist, and cannot be saved: " + id);
            return;
        }
        configLoader.save(module, domain, id);
    }
}
