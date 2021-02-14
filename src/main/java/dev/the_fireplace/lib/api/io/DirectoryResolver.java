package dev.the_fireplace.lib.api.io;

import dev.the_fireplace.lib.impl.io.Directories;
import net.minecraft.server.MinecraftServer;

import java.nio.file.Path;

public interface DirectoryResolver {
    static DirectoryResolver getInstance() {
        //noinspection deprecation
        return Directories.INSTANCE;
    }

    Path getSavePath();
    Path getSavePath(MinecraftServer server);
    Path getConfigPath();
    String getLangDirectory(String modid);
}
