package dev.the_fireplace.lib.lazyio;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.lazyio.injectables.ReloadableManager;
import dev.the_fireplace.lib.api.lazyio.interfaces.Reloadable;
import io.netty.util.internal.ConcurrentSet;

import javax.inject.Singleton;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

@Implementation
@Singleton
public final class ReloadableObjectManager implements ReloadableManager {
    private final ConcurrentHashMap<String, Collection<Reloadable>> reloadableGroups = new ConcurrentHashMap<>(1);

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
