package dev.the_fireplace.lib.impl.io;

import dev.the_fireplace.annotateddi.di.Implementation;
import dev.the_fireplace.lib.api.io.injectables.DirectoryResolver;
import dev.the_fireplace.lib.impl.FireplaceLib;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;

import javax.inject.Singleton;
import java.io.File;
import java.nio.file.Path;

@Implementation
@Singleton
public final class Directories implements DirectoryResolver {

    @Override
    public Path getSavePath() {
        return getSavePath(FireplaceLib.getServer());
    }

    @Override
    public Path getSavePath(MinecraftServer server) {
        return server.getSavePath(WorldSavePath.ROOT);
    }

    @Override
    public Path getConfigPath() {
        return FabricLoader.getInstance().getConfigDir();
    }

    @Override
    public String getLangDirectory(String modid) {
        return File.separatorChar + "assets" + File.separatorChar + modid + File.separatorChar + "lang" + File.separatorChar;
    }
}
