package the_fireplace.lib.api.io;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.dimension.DimensionType;
import the_fireplace.lib.impl.FireplaceLib;

import java.nio.file.Path;

public final class Directories {
    public static Path getSaveDirectory() {
        return getSaveDirectory(FireplaceLib.getServer());
    }

    public static Path getSaveDirectory(MinecraftServer server) {
        return server.getWorld(DimensionType.OVERWORLD).getSaveHandler().getWorldDir().toPath();
    }

    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }

    public static String getLangDirectory(String modid) {
        return "/assets/" + modid + "/lang/";
    }
}
