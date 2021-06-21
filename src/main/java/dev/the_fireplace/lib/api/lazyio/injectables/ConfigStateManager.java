package dev.the_fireplace.lib.api.lazyio.injectables;

import dev.the_fireplace.lib.api.lazyio.interfaces.Config;

public interface ConfigStateManager {
    <T extends Config> T initialize(T config);
    void load(Config config);
    void reload(Config config);
    void save(Config config);
}
