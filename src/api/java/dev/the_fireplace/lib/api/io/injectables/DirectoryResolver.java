package dev.the_fireplace.lib.api.io.injectables;

import net.minecraft.server.MinecraftServer;

import java.nio.file.Path;

public interface DirectoryResolver
{
    Path getSavePath();

    Path getSavePath(MinecraftServer server);

    Path getConfigPath();

    String getLangDirectory(String modid);
}
