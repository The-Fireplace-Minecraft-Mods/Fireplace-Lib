package dev.the_fireplace.lib.environment;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.environment.injectables.ModList;
import net.fabricmc.loader.api.FabricLoader;

import java.util.Collection;
import java.util.stream.Collectors;

@Implementation
public final class FabricModList implements ModList
{
    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public Collection<String> getLoadedMods() {
        return FabricLoader.getInstance().getAllMods().stream()
            .map(mod -> mod.getMetadata().getId())
            .collect(Collectors.toUnmodifiableSet());
    }
}
