package dev.the_fireplace.lib;

import com.google.inject.Injector;
import dev.the_fireplace.annotateddi.api.Injectors;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

public final class FireplaceLibConstants
{
    public static final String MODID = "fireplacelib";
    public static final ResourceLocation PACKET_CHANNEL_ID = ResourceLocation.fromNamespaceAndPath(MODID, "packets");

    private static final Logger LOGGER = LogManager.getLogger(MODID);

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

    public static Injector getInjector() {
        return Injectors.INSTANCE.getAutoInjector(FireplaceLibConstants.MODID);
    }
}
