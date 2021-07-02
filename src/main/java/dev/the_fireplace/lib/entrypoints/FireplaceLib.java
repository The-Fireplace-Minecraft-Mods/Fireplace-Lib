package dev.the_fireplace.lib.entrypoints;

import com.google.inject.Injector;
import dev.the_fireplace.annotateddi.api.entrypoints.DIModInitializer;
import dev.the_fireplace.lib.init.FireplaceLibInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

public final class FireplaceLib implements DIModInitializer {
    public static final String MODID = "fireplacelib";

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

    private static final Logger LOGGER = LogManager.getLogger(MODID);
    public static Logger getLogger() {
        return LOGGER;
    }

    public static boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public void onInitialize(Injector container) {
        container.getInstance(FireplaceLibInitializer.class).init();
    }
}
