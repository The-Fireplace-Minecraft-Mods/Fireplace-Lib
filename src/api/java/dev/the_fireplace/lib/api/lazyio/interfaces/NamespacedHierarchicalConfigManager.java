package dev.the_fireplace.lib.api.lazyio.interfaces;

import net.minecraft.util.Identifier;

public interface NamespacedHierarchicalConfigManager<T extends HierarchicalConfig> {
    Iterable<Identifier> getAllowedModuleIds();

    T get(Identifier moduleId);

    boolean isCustom(Identifier moduleId);

    void addCustom(Identifier moduleId, T module);
    
    boolean deleteCustom(Identifier moduleId);

    void saveAllCustoms();

    void saveCustom(Identifier id);
}
