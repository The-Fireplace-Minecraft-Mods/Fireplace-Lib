package dev.the_fireplace.lib.api.environment.injectables;

import java.util.Collection;

public interface ModList
{
    boolean isModLoaded(String modId);

    Collection<String> getLoadedMods();
}
