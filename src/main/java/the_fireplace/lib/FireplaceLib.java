package the_fireplace.lib;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import the_fireplace.lib.api.chat.TranslationService;
import the_fireplace.lib.api.multithreading.ConcurrentExecutionManager;
import the_fireplace.lib.events.NetworkEvents;

public class FireplaceLib implements ModInitializer {
    public static final String MODID = "fireplacelib";
    private static MinecraftServer minecraftServer;

    public static MinecraftServer getServer() {
        return minecraftServer;
    }

    @Override
    public void onInitialize() {
        TranslationService.initialize(MODID);
        ServerLifecycleEvents.SERVER_STARTING.register(s -> minecraftServer = s);
        NetworkEvents.init();
        ServerLifecycleEvents.SERVER_STOPPING.register(s -> {
            //TODO save data
            try {
                ConcurrentExecutionManager.waitForCompletion();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
