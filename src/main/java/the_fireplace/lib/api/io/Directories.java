package the_fireplace.lib.api.io;

import net.minecraft.util.WorldSavePath;
import the_fireplace.lib.impl.FireplaceLib;

import java.io.File;
import java.nio.file.Path;

public final class Directories {
    public static final Path SAVE_DIRECTORY = FireplaceLib.getServer().getSavePath(WorldSavePath.ROOT);
    public static final File CONFIG_DIRECTORY = new File("config");

    public static String getLangDirectory(String modid) {
        return "/assets/" + modid + "/lang/";
    }
}
