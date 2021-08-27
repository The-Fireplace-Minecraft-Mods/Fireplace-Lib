package dev.the_fireplace.lib.api.lazyio.injectables;

import dev.the_fireplace.lib.api.lazyio.interfaces.HierarchicalConfig;
import dev.the_fireplace.lib.api.lazyio.interfaces.HierarchicalConfigManager;

public interface HierarchicalConfigManagerFactory {
    /**
     * Create a hierarchical config manager. With this, you can have a default/fallback config, and custom defined configs that override it for specific cases.
     * @param domain
     *  The config domain, will be used as the folder name the custom configs are created in, and the reloadable group which all of the custom defined configs belong to.
     * @param defaultConfig
     *  The default config object. You will need to manage the loading, saving, etc of this. I recommend making the default config double as a {@link dev.the_fireplace.lib.api.lazyio.interfaces.Config} and registering it with {@link ConfigStateManager}.
     * @param allowedModuleIds
     *  The allowed IDs that can be used to override the default config. These are used to scan for which existing overrides can be loaded/reloaded from the config/domain folder.
     */
    <E extends HierarchicalConfig> HierarchicalConfigManager<E> create(String domain, E defaultConfig, Iterable<String> allowedModuleIds);
}
