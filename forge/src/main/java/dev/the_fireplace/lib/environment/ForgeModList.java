package dev.the_fireplace.lib.environment;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.environment.injectables.ModList;
import net.minecraftforge.forgespi.language.IModInfo;

import java.util.Collection;
import java.util.stream.Collectors;

@Implementation
public final class ForgeModList implements ModList
{
    @Override
    public boolean isModLoaded(String modId) {
        return net.minecraftforge.fml.ModList.get().isLoaded(modId);
    }

    @Override
    public Collection<String> getLoadedMods() {
        return net.minecraftforge.fml.ModList.get().getMods().stream()
            .map(IModInfo::getModId)
            .collect(Collectors.toUnmodifiableSet());
    }
}
