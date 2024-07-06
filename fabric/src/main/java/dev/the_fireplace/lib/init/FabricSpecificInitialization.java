package dev.the_fireplace.lib.init;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.lifecycle.injectables.ServerLifecycle;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

import java.util.function.Consumer;

@Implementation
public final class FabricSpecificInitialization implements ServerLifecycle
{
    @Override
    public void registerServerStartingCallback(Consumer<MinecraftServer> runnable) {
        ServerLifecycleEvents.SERVER_STARTING.register(runnable::accept);
    }

    @Override
    public void registerServerStoppingCallback(Consumer<MinecraftServer> runnable) {
        ServerLifecycleEvents.SERVER_STOPPING.register(runnable::accept);
    }

    @Override
    public void registerServerStoppedCallback(Consumer<MinecraftServer> runnable) {
        ServerLifecycleEvents.SERVER_STOPPED.register(runnable::accept);
    }
}
