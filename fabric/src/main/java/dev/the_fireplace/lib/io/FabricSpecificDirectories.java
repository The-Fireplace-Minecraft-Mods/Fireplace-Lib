package dev.the_fireplace.lib.io;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.domain.io.LoaderSpecificDirectories;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.nio.file.Path;
import java.util.Optional;

@Implementation
public final class FabricSpecificDirectories implements LoaderSpecificDirectories
{
    @Override
    public Path getConfigPath() {
        return FabricLoader.getInstance().getConfigDir();
    }

    @Override
    public Optional<Path> getResource(String modId, String path) {
        Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer(modId);
        if (modContainer.isEmpty()) {
            return Optional.empty();
        }

        return modContainer.get().findPath(path);
    }
}
