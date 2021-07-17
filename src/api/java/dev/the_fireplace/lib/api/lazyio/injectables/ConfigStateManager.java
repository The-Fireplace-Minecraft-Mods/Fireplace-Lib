package dev.the_fireplace.lib.api.lazyio.injectables;

import dev.the_fireplace.lib.api.lazyio.interfaces.Config;

public interface ConfigStateManager {
    /**
     * Sets up the config. Loads, saves,
     * and registers the config so it can be reloaded with {@link ReloadableManager#reload(String)} using reload group configId (or subfolder/configId if subfolder is present)
     */
    <T extends Config> T initialize(T config);
    void load(Config config);
    void reload(Config config);
    void save(Config config);
}
