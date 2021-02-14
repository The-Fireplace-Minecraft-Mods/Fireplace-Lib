package dev.the_fireplace.lib.impl;

import dev.the_fireplace.lib.api.chat.TranslatorManager;
import dev.the_fireplace.lib.api.io.SaveTimer;
import dev.the_fireplace.lib.api.multithreading.ExecutionManager;
import dev.the_fireplace.lib.impl.network.NetworkEvents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FireplaceLib implements ModInitializer {
    public static final String MODID = "fireplacelib";

    private static MinecraftServer minecraftServer = null;
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
        TranslatorManager.getInstance().addTranslator(MODID);
        NetworkEvents.init();
        ServerLifecycleEvents.SERVER_STARTING.register(s -> {
            minecraftServer = s;
            ExecutionManager.getInstance().startExecutors();
            SaveTimer.getInstance().resetTimer();
        });
        ServerLifecycleEvents.SERVER_STOPPING.register(s -> {
            SaveTimer.getInstance().prepareForServerShutdown();
            try {
                ExecutionManager.getInstance().waitForCompletion();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
