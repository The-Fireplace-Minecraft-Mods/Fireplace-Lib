package dev.the_fireplace.lib;

import dev.the_fireplace.lib.api.chat.injectables.TranslatorFactory;
import dev.the_fireplace.lib.api.lazyio.injectables.SaveTimer;
import dev.the_fireplace.lib.api.multithreading.injectables.ExecutionManager;
import dev.the_fireplace.lib.commands.FLCommands;
import dev.the_fireplace.lib.network.NetworkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class FireplaceLibInitializer {

    private final TranslatorFactory translatorFactory;
    private final ExecutionManager executionManager;
    private final FLCommands fireplaceLibCommands;
    private final SaveTimer saveTimer;
    private final NetworkEvents networkEvents;

    @Inject
    public FireplaceLibInitializer(
        TranslatorFactory translatorFactory,
        ExecutionManager executionManager,
        FLCommands fireplaceLibCommands,
        SaveTimer saveTimer,
        NetworkEvents networkEvents
    ) {
        this.translatorFactory = translatorFactory;
        this.executionManager = executionManager;
        this.fireplaceLibCommands = fireplaceLibCommands;
        this.saveTimer = saveTimer;
        this.networkEvents = networkEvents;
    }

    void init() {
        translatorFactory.addTranslator(FireplaceLib.MODID);
        networkEvents.init();
        ServerLifecycleEvents.SERVER_STARTING.register(s -> {
            FireplaceLib.minecraftServer = s;
            executionManager.startExecutors();
            saveTimer.resetTimer();
            fireplaceLibCommands.register(s);
        });
        ServerLifecycleEvents.SERVER_STOPPING.register(s -> {
            saveTimer.prepareForServerShutdown();
            try {
                executionManager.waitForCompletion();
            } catch (InterruptedException e) {
                FireplaceLib.getLogger().error("Interrupted while trying to wait for execution manager to complete.", e);
            }
        });
    }
}
