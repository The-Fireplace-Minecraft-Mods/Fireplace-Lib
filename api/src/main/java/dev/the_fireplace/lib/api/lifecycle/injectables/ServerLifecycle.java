package dev.the_fireplace.lib.api.lifecycle.injectables;

import net.minecraft.server.MinecraftServer;

import java.util.function.Consumer;

public interface ServerLifecycle
{
    void registerServerStartingCallback(Consumer<MinecraftServer> runnable);

    void registerServerStoppingCallback(Consumer<MinecraftServer> runnable);

    void registerServerStoppedCallback(Consumer<MinecraftServer> runnable);
}
