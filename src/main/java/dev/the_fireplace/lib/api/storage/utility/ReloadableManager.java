package dev.the_fireplace.lib.api.storage.utility;

import dev.the_fireplace.lib.api.storage.Reloadable;
import dev.the_fireplace.lib.impl.storage.utility.ReloadableObjectManager;

public interface ReloadableManager {
    static ReloadableManager getInstance() {
        //noinspection deprecation
        return ReloadableObjectManager.INSTANCE;
    }

    void register(Reloadable reloadable);

    boolean unregister(Reloadable reloadable);

    boolean reload(String reloadableGroup);
}
