package dev.the_fireplace.lib.init;

import dev.the_fireplace.lib.FireplaceLibConstants;
import dev.the_fireplace.lib.api.chat.injectables.TranslatorFactory;
import dev.the_fireplace.lib.api.lazyio.injectables.SaveTimer;
import dev.the_fireplace.lib.api.multithreading.injectables.ExecutionManager;
import dev.the_fireplace.lib.command.FLCommands;
import dev.the_fireplace.lib.api.lifecycle.injectables.ServerLifecycle;
import dev.the_fireplace.lib.network.NetworkEvents;
import net.minecraft.server.MinecraftServer;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class FireplaceLibInitializer
{
    private boolean initialized = false;
    private final TranslatorFactory translatorFactory;
    private final ExecutionManager executionManager;
    private final FLCommands fireplaceLibCommands;
    private final SaveTimer saveTimer;
    private final ServerLifecycle serverLifecycle;
    private final NetworkEvents networkEvents;

    @Inject
    public FireplaceLibInitializer(
        TranslatorFactory translatorFactory,
        ExecutionManager executionManager,
        FLCommands fireplaceLibCommands,
        SaveTimer saveTimer,
        ServerLifecycle serverLifecycle,
        NetworkEvents networkEvents
    ) {
        this.translatorFactory = translatorFactory;
        this.executionManager = executionManager;
        this.fireplaceLibCommands = fireplaceLibCommands;
        this.saveTimer = saveTimer;
        this.serverLifecycle = serverLifecycle;
        this.networkEvents = networkEvents;
    }

    public void init() {
        if (!initialized) {
            initialized = true;
            translatorFactory.addTranslator(FireplaceLibConstants.MODID);
            networkEvents.init();
            serverLifecycle.registerServerStartingCallback(this::onServerStarting);
            serverLifecycle.registerServerStoppingCallback(this::onServerStopping);
            serverLifecycle.registerServerStoppedCallback(s -> FireplaceLibConstants.setMinecraftServer(null));
        }
    }

    private void onServerStarting(MinecraftServer server) {
        FireplaceLibConstants.setMinecraftServer(server);
        executionManager.startExecutors();
        saveTimer.resetTimer();
        fireplaceLibCommands.register(server);
    }

    private void onServerStopping(MinecraftServer server) {
        saveTimer.prepareForServerShutdown();
        try {
            executionManager.waitForCompletion();
        } catch (InterruptedException e) {
            FireplaceLibConstants.getLogger().error("Interrupted while trying to wait for execution manager to complete.", e);
        }
    }
}
