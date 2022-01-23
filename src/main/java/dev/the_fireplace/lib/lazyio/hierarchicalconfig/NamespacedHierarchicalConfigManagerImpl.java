package dev.the_fireplace.lib.lazyio.hierarchicalconfig;

import dev.the_fireplace.lib.api.lazyio.injectables.ReloadableManager;
import dev.the_fireplace.lib.api.lazyio.interfaces.HierarchicalConfig;
import dev.the_fireplace.lib.api.lazyio.interfaces.NamespacedHierarchicalConfigManager;
import dev.the_fireplace.lib.api.lazyio.interfaces.Reloadable;
import dev.the_fireplace.lib.entrypoints.FireplaceLib;
import dev.the_fireplace.lib.io.access.JsonStoragePath;
import net.minecraft.util.Identifier;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class NamespacedHierarchicalConfigManagerImpl<T extends HierarchicalConfig> implements NamespacedHierarchicalConfigManager<T>
{

    protected final HierarchicalConfigLoader configLoader;
    protected final JsonStoragePath jsonStoragePath;
    protected final ReloadableManager reloadableManager;

    protected final String domain;
    protected final T defaultConfig;
    protected final Iterable<Identifier> allowedModuleIds;
    protected final Map<Identifier, T> modules;

    public NamespacedHierarchicalConfigManagerImpl(
        String domain,
        T defaultConfig,
        Iterable<Identifier> allowedModuleIds,
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

        loadExistingHierarchy(allowedModuleIds);
        registerHierarchyReloadable();
    }

    protected void loadExistingHierarchy(Iterable<Identifier> allowedModuleIds) {
        for (Identifier id : allowedModuleIds) {
            Path filePath = jsonStoragePath.resolveConfigBasedJsonFilePath(domain, id);
            if (filePath.toFile().exists() && !modules.containsKey(id)) {
                HierarchicalConfig module = defaultConfig.clone();
                try {
                    //noinspection unchecked
                    addCustom(id, (T) module);
                } catch (ClassCastException exception) {
                    FireplaceLib.getLogger().error("Cloned hierarchical config was not of the expected type!", exception);
                }
            }
        }
    }

    protected void registerHierarchyReloadable() {
        reloadableManager.register(new Reloadable()
        {
            @Override
            public void reload() {
                loadExistingHierarchy(getAllowedModuleIds());
            }

            @Override
            public String getReloadGroup() {
                return domain;
            }
        });
    }

    @Override
    public Iterable<Identifier> getAllowedModuleIds() {
        return allowedModuleIds;
    }

    @Override
    public T get(Identifier moduleId) {
        return modules.getOrDefault(moduleId, defaultConfig);
    }

    @Override
    public Collection<Identifier> getCustoms() {
        return Set.copyOf(modules.keySet());
    }

    @Override
    public boolean isCustom(Identifier moduleId) {
        return modules.containsKey(moduleId);
    }

    @Override
    public void addCustom(Identifier moduleId, T module) {
        modules.put(moduleId, module);
        configLoader.initialize(module, domain, moduleId);
    }

    @Override
    public boolean deleteCustom(Identifier moduleId) {
        T module = modules.remove(moduleId);
        if (module == null) {
            return false;
        }

        return configLoader.delete(domain, moduleId);
    }

    @Override
    public void saveAllCustoms() {
        for (Map.Entry<Identifier, T> moduleEntry : modules.entrySet()) {
            configLoader.save(moduleEntry.getValue(), domain, moduleEntry.getKey());
        }
    }

    @Override
    public void saveCustom(Identifier id) {
        T module = modules.get(id);
        if (module == null) {
            FireplaceLib.getLogger().error("Custom config does not exist, and cannot be saved: " + id);
            return;
        }
        configLoader.save(module, domain, id);
    }
}
