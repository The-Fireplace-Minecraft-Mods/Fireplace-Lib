package the_fireplace.lib.impl;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import the_fireplace.lib.api.chat.TranslationService;
import the_fireplace.lib.api.io.SaveTimer;
import the_fireplace.lib.api.multithreading.ConcurrentExecutionManager;
import the_fireplace.lib.impl.events.NetworkEvents;

public class FireplaceLib implements ModInitializer {
    public static final String MODID = "fireplacelib";

    private static MinecraftServer minecraftServer;
    public static MinecraftServer getServer() {
        return minecraftServer;
    }

    private static final Logger LOGGER = LogManager.getLogger(MODID);
    public static Logger getLogger() {
        return LOGGER;
    }

    @Override
    public void onInitialize() {
        TranslationService.initialize(MODID);
        NetworkEvents.init();
        ServerLifecycleEvents.SERVER_STARTING.register(s -> {
            minecraftServer = s;
            ConcurrentExecutionManager.startExecutors();
            SaveTimer.resetTimer();
        });
        ServerLifecycleEvents.SERVER_STOPPING.register(s -> {
            SaveTimer.prepareForServerShutdown();
            try {
                ConcurrentExecutionManager.waitForCompletion();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
