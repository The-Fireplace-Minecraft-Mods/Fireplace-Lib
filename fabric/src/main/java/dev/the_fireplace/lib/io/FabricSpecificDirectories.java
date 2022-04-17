package dev.the_fireplace.lib.io;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.domain.io.LoaderSpecificDirectories;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

@Implementation
public final class FabricSpecificDirectories implements LoaderSpecificDirectories
{
    @Override
    public Path getConfigPath() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
