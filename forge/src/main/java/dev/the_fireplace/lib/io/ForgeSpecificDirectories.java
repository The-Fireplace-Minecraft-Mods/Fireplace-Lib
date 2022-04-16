package dev.the_fireplace.lib.io;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.domain.io.LoaderSpecificDirectories;
import net.minecraftforge.fml.loading.FMLConfig;

import java.nio.file.Path;

@Implementation
public final class ForgeSpecificDirectories implements LoaderSpecificDirectories
{
    @Override
    public Path getConfigPath() {
        return Path.of(FMLConfig.defaultConfigPath());
    }
}
