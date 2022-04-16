package dev.the_fireplace.lib.api.lazyio.interfaces;

import java.util.Collection;

public interface HierarchicalConfigManager<T extends HierarchicalConfig>
{
    Iterable<String> getAllowedModuleIds();

    T get(String moduleId);

    Collection<String> getCustoms();

    boolean isCustom(String moduleId);

    void addCustom(String moduleId, T module);

    boolean deleteCustom(String moduleId);

    void saveAllCustoms();

    void saveCustom(String id);
}
