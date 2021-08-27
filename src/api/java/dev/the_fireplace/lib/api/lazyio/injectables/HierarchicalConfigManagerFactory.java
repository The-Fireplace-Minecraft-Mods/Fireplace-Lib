package dev.the_fireplace.lib.api.lazyio.injectables;

import dev.the_fireplace.lib.api.lazyio.interfaces.HierarchicalConfig;
import dev.the_fireplace.lib.api.lazyio.interfaces.HierarchicalConfigManager;
import dev.the_fireplace.lib.api.lazyio.interfaces.NamespacedHierarchicalConfigManager;
import net.minecraft.util.Identifier;

import java.util.concurrent.Callable;

public interface HierarchicalConfigManagerFactory {
    /**
     * Create a hierarchical config manager. With this, you can have a default/fallback config, and custom defined configs that override it for specific cases.
     *
     * @param domain           The config domain, will be used as the folder name the custom configs are created in, and the reloadable group which all of the custom defined configs belong to.
     * @param defaultConfig    The default config object. You will need to manage the loading, saving, etc of this. I recommend making the default config double as a {@link dev.the_fireplace.lib.api.lazyio.interfaces.Config} and registering it with {@link ConfigStateManager}.
     * @param allowedModuleIds The allowed IDs that can be used to override the default config. These are used to scan for which existing overrides can be loaded/reloaded from the config/domain folder.
     */
    <E extends HierarchicalConfig> HierarchicalConfigManager<E> create(String domain, E defaultConfig, Iterable<String> allowedModuleIds);

    /**
     * Create a hierarchical config manager based around {@link Identifier} IDs instead of Strings. Each identifier's domain will have a folder within the config/domain folder.
     *
     * @see HierarchicalConfigManagerFactory#create
     */
    <E extends HierarchicalConfig> NamespacedHierarchicalConfigManager<E> createNamespaced(String domain, E defaultConfig, Iterable<Identifier> allowedModuleIds);

    /**
     * Create a dynamic namespaced hierarchical config manager. It is similar to a standard namespaced config manager, except the domain of allowed module IDs can change after initial creation. Use {@link ReloadableManager} with group "dynamic_(domain)" to notify it that the list of allowed IDs may have changed.
     *
     * @see HierarchicalConfigManagerFactory#createNamespaced
     */
    <E extends HierarchicalConfig> NamespacedHierarchicalConfigManager<E> createDynamicNamespaced(String domain, E defaultConfig, Iterable<Identifier> defaultAllowedModuleIds, Callable<Iterable<Identifier>> getAllowedModuleIds);
}
