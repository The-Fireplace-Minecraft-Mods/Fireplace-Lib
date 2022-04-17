package dev.the_fireplace.lib.domain.init;

import net.minecraft.server.MinecraftServer;

import java.util.function.Consumer;

public interface LoaderSpecificInitialization
{
    void initNetwork();

    void registerServerStartingCallback(Consumer<MinecraftServer> runnable);

    void registerServerStoppingCallback(Consumer<MinecraftServer> runnable);

    void registerServerStoppedCallback(Consumer<MinecraftServer> runnable);
}
