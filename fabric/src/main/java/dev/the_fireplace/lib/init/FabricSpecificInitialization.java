package dev.the_fireplace.lib.init;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.domain.init.LoaderSpecificInitialization;
import dev.the_fireplace.lib.network.FabricNetworkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

import javax.inject.Inject;
import java.util.function.Consumer;

@Implementation
public final class FabricSpecificInitialization implements LoaderSpecificInitialization
{
    private final FabricNetworkEvents fabricNetworkEvents;

    @Inject
    public FabricSpecificInitialization(FabricNetworkEvents fabricNetworkEvents) {
        this.fabricNetworkEvents = fabricNetworkEvents;
    }

    @Override
    public void initNetwork() {
        fabricNetworkEvents.init();
    }

    @Override
    public void registerServerStartingCallback(Consumer<MinecraftServer> runnable) {
        ServerLifecycleEvents.SERVER_STARTING.register((ServerLifecycleEvents.ServerStarting) runnable);
    }

    @Override
    public void registerServerStoppingCallback(Consumer<MinecraftServer> runnable) {
        ServerLifecycleEvents.SERVER_STOPPING.register((ServerLifecycleEvents.ServerStopping) runnable);
    }

    @Override
    public void registerServerStoppedCallback(Consumer<MinecraftServer> runnable) {
        ServerLifecycleEvents.SERVER_STOPPED.register((ServerLifecycleEvents.ServerStopped) runnable);
    }
}
