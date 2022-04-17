package dev.the_fireplace.lib.lazyio.hierarchicalconfig;

import dev.the_fireplace.lib.api.lazyio.injectables.ReloadableManager;
import dev.the_fireplace.lib.api.lazyio.interfaces.HierarchicalConfig;
import dev.the_fireplace.lib.api.lazyio.interfaces.Reloadable;
import dev.the_fireplace.lib.io.access.JsonStoragePath;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

public final class DynamicNamespacedHierarchicalConfigManager<T extends HierarchicalConfig> extends NamespacedHierarchicalConfigManagerImpl<T> implements Reloadable
{

    private final Callable<Iterable<ResourceLocation>> getAllowedModuleIds;
    private final AtomicBoolean hasPendingUpdates = new AtomicBoolean(true);

    public DynamicNamespacedHierarchicalConfigManager(
        String domain,
        T defaultConfig,
        Iterable<ResourceLocation> defaultAllowedModuleIds,
        Callable<Iterable<ResourceLocation>> getAllowedModuleIds,
        HierarchicalConfigLoader configLoader,
        JsonStoragePath jsonStoragePath,
        ReloadableManager reloadableManager
    ) {
        super(domain, defaultConfig, defaultAllowedModuleIds, configLoader, jsonStoragePath, reloadableManager);
        this.getAllowedModuleIds = getAllowedModuleIds;
        reloadableManager.register(this);
    }

    @Override
    public Iterable<ResourceLocation> getAllowedModuleIds() {
        try {
            return getAllowedModuleIds.call();
        } catch (Exception exception) {
            return super.getAllowedModuleIds();
        }
    }

    public void markUpdated() {
        hasPendingUpdates.lazySet(true);
    }

    private void getUpdatedModuleList() {
        if (hasPendingUpdates.get()) {
            hasPendingUpdates.lazySet(false);
            loadExistingHierarchy(getAllowedModuleIds());
        }
    }

    @Override
    public T get(ResourceLocation moduleId) {
        getUpdatedModuleList();
        return super.get(moduleId);
    }

    @Override
    public Collection<ResourceLocation> getCustoms() {
        getUpdatedModuleList();
        return super.getCustoms();
    }

    @Override
    public boolean isCustom(ResourceLocation moduleId) {
        getUpdatedModuleList();
        return super.isCustom(moduleId);
    }

    @Override
    public void saveAllCustoms() {
        getUpdatedModuleList();
        super.saveAllCustoms();
    }

    @Override
    public void saveCustom(ResourceLocation id) {
        getUpdatedModuleList();
        super.saveCustom(id);
    }

    @Override
    public void reload() {
        markUpdated();
    }

    @Override
    public String getReloadGroup() {
        return "dynamic_" + domain;
    }
}
