package dev.the_fireplace.lib.api.storage.utility;

import dev.the_fireplace.lib.api.storage.Reloadable;

public interface ReloadableManager {
    void register(Reloadable reloadable);

    boolean unregister(Reloadable reloadable);

    boolean reload(String reloadableGroup);
}
