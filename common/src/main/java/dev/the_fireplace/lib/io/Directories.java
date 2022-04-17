package dev.the_fireplace.lib.io;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.FireplaceLibConstants;
import dev.the_fireplace.lib.api.io.injectables.DirectoryResolver;
import dev.the_fireplace.lib.domain.io.LoaderSpecificDirectories;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.nio.file.Path;

@Implementation
@Singleton
public final class Directories implements DirectoryResolver
{
    private final LoaderSpecificDirectories loaderSpecificDirectories;

    @Inject
    public Directories(LoaderSpecificDirectories loaderSpecificDirectories) {
        this.loaderSpecificDirectories = loaderSpecificDirectories;
    }

    @Override
    public Path getSavePath() {
        return getSavePath(FireplaceLibConstants.getServer());
    }

    @Override
    public Path getSavePath(MinecraftServer server) {
        return server.getWorldPath(LevelResource.ROOT);
    }

    @Override
    public Path getConfigPath() {
        return loaderSpecificDirectories.getConfigPath();
    }

    @Override
    public String getLangDirectory(String modid) {
        return "/assets/" + modid + "/lang/";
    }
}
