package dev.the_fireplace.lib.api.storage.injectables;

import dev.the_fireplace.lib.api.storage.interfaces.Reloadable;

public interface ReloadableManager {
    void register(Reloadable reloadable);

    boolean unregister(Reloadable reloadable);

    boolean reload(String reloadableGroup);
}
