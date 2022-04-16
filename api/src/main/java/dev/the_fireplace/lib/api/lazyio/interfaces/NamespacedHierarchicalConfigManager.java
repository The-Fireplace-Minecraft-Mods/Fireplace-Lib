package dev.the_fireplace.lib.api.lazyio.interfaces;

import net.minecraft.resources.ResourceLocation;

import java.util.Collection;

public interface NamespacedHierarchicalConfigManager<T extends HierarchicalConfig>
{
    Iterable<ResourceLocation> getAllowedModuleIds();

    T get(ResourceLocation moduleId);

    Collection<ResourceLocation> getCustoms();

    boolean isCustom(ResourceLocation moduleId);

    void addCustom(ResourceLocation moduleId, T module);

    boolean deleteCustom(ResourceLocation moduleId);

    void saveAllCustoms();

    void saveCustom(ResourceLocation id);
}
