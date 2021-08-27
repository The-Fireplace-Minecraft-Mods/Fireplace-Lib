package dev.the_fireplace.lib.api.lazyio.interfaces;

public interface HierarchicalConfigManager<T extends HierarchicalConfig> {
    Iterable<String> getAllowedModuleIds();

    T get(String moduleId);

    boolean isCustom(String moduleId);

    void addCustom(String moduleId, T module);
    
    boolean deleteCustom(String moduleId);
}
