package dev.the_fireplace.lib.api.lazyio.injectables;

import dev.the_fireplace.lib.api.lazyio.interfaces.Reloadable;

public interface ReloadableManager {
    void register(Reloadable reloadable);

    boolean unregister(Reloadable reloadable);

    boolean reload(String reloadableGroup);
}
