package dev.the_fireplace.lib.lazyio.hierarchicalconfig;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.lazyio.injectables.HierarchicalConfigManagerFactory;
import dev.the_fireplace.lib.api.lazyio.injectables.ReloadableManager;
import dev.the_fireplace.lib.api.lazyio.interfaces.HierarchicalConfig;
import dev.the_fireplace.lib.api.lazyio.interfaces.HierarchicalConfigManager;
import dev.the_fireplace.lib.api.lazyio.interfaces.NamespacedHierarchicalConfigManager;
import dev.the_fireplace.lib.io.access.JsonStoragePath;
import net.minecraft.resources.ResourceLocation;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.Callable;

@Singleton
@Implementation
public final class HierarchicalConfigManagerFactoryImpl implements HierarchicalConfigManagerFactory
{

    private final HierarchicalConfigLoader configLoader;
    private final JsonStoragePath jsonStoragePath;
    private final ReloadableManager reloadableManager;

    @Inject
    public HierarchicalConfigManagerFactoryImpl(HierarchicalConfigLoader configLoader, JsonStoragePath jsonStoragePath, ReloadableManager reloadableManager) {
        this.configLoader = configLoader;
        this.jsonStoragePath = jsonStoragePath;
        this.reloadableManager = reloadableManager;
    }

    @Override
    public <E extends HierarchicalConfig> HierarchicalConfigManager<E> create(String domain, E defaultConfig, Iterable<String> allowedModuleIds) {
        return new HierarchicalConfigManagerImpl<>(domain, defaultConfig, allowedModuleIds, configLoader, jsonStoragePath, reloadableManager);
    }

    @Override
    public <E extends HierarchicalConfig> NamespacedHierarchicalConfigManager<E> createNamespaced(String domain, E defaultConfig, Iterable<ResourceLocation> allowedModuleIds) {
        return new NamespacedHierarchicalConfigManagerImpl<>(domain, defaultConfig, allowedModuleIds, configLoader, jsonStoragePath, reloadableManager);
    }

    @Override
    public <E extends HierarchicalConfig> NamespacedHierarchicalConfigManager<E> createDynamicNamespaced(String domain, E defaultConfig, Iterable<ResourceLocation> defaultAllowedModuleIds, Callable<Iterable<ResourceLocation>> getAllowedModuleIds) {
        return new DynamicNamespacedHierarchicalConfigManager<>(domain, defaultConfig, defaultAllowedModuleIds, getAllowedModuleIds, configLoader, jsonStoragePath, reloadableManager);
    }
}
