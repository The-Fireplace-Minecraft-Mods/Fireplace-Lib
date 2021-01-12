package the_fireplace.lib.api.io;

import net.minecraft.server.MinecraftServer;
import the_fireplace.lib.impl.io.Directories;

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
