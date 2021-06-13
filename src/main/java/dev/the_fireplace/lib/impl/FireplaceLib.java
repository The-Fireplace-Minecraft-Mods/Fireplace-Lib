package dev.the_fireplace.lib.impl;

import dev.the_fireplace.annotateddi.AnnotatedDI;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class FireplaceLib implements ModInitializer {
    public static final String MODID = "fireplacelib";

    static MinecraftServer minecraftServer = null;
    public static MinecraftServer getServer() {
        if (minecraftServer == null) {
            throw new IllegalStateException("Attempted to get server before it starts!");
        }
        return minecraftServer;
    }

    private static final Logger LOGGER = LogManager.getLogger(MODID);
    public static Logger getLogger() {
        return LOGGER;
    }

    @Override
    public void onInitialize() {
        AnnotatedDI.getInjector().getInstance(FireplaceLibInitializer.class).init();
    }
}
