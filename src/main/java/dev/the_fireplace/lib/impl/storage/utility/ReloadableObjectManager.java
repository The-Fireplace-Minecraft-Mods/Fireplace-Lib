package dev.the_fireplace.lib.impl.storage.utility;

import dev.the_fireplace.lib.api.storage.Reloadable;
import dev.the_fireplace.lib.api.storage.utility.ReloadableManager;
import io.netty.util.internal.ConcurrentSet;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public final class ReloadableObjectManager implements ReloadableManager {
    @Deprecated
    public static final ReloadableManager INSTANCE = new ReloadableObjectManager();

    private final ConcurrentHashMap<String, Collection<Reloadable>> reloadableGroups = new ConcurrentHashMap<>(1);

    private ReloadableObjectManager() {}

    @Override
    public void register(Reloadable reloadable) {
        reloadableGroups.computeIfAbsent(reloadable.getReloadGroup(), unused -> new ConcurrentSet<>())
            .add(reloadable);
    }

    @Override
    public boolean unregister(Reloadable reloadable) {
        return reloadableGroups.computeIfAbsent(reloadable.getReloadGroup(), unused -> new ConcurrentSet<>())
            .remove(reloadable);
    }

    @Override
    public boolean reload(String reloadableGroup) {
        if (reloadableGroups.containsKey(reloadableGroup) && !reloadableGroups.get(reloadableGroup).isEmpty()) {
            for (Reloadable reloadable : reloadableGroups.get(reloadableGroup)) {
                reloadable.reload();
            }
            return true;
        }
        return false;
    }
}
