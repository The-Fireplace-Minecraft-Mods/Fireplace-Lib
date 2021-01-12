package the_fireplace.lib.impl;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import the_fireplace.lib.api.chat.TranslatorManager;
import the_fireplace.lib.api.io.SaveTimer;
import the_fireplace.lib.api.multithreading.ConcurrentExecutionManager;
import the_fireplace.lib.impl.events.NetworkEvents;

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
            ConcurrentExecutionManager.startExecutors();
            SaveTimer.getInstance().resetTimer();
        });
        ServerLifecycleEvents.SERVER_STOPPING.register(s -> {
            SaveTimer.getInstance().prepareForServerShutdown();
            try {
                ConcurrentExecutionManager.waitForCompletion();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
