package dev.the_fireplace.lib;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

public final class FireplaceLibConstants
{
    public static final String MODID = "fireplacelib";

    private static Logger LOGGER = LogManager.getLogger(MODID);

    @Nullable
    private static MinecraftServer minecraftServer = null;

    public static MinecraftServer getServer() {
        if (minecraftServer == null) {
            throw new IllegalStateException("Attempted to get server before it starts!");
        }
        return minecraftServer;
    }

    public static void setMinecraftServer(@Nullable MinecraftServer server) {
        if (minecraftServer == null || server == null) {
            minecraftServer = server;
        }
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    public static void setLogger(Logger logger) {
        LOGGER = logger;
    }

    public static boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }
}
