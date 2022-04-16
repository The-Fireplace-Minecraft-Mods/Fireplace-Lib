package dev.the_fireplace.lib.init;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.domain.init.LoaderSpecificInitialization;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.function.Consumer;

@Implementation
public final class ForgeSpecificInitialization implements LoaderSpecificInitialization
{
    @Override
    public void registerServerStartingCallback(Consumer<MinecraftServer> runnable) {
        MinecraftForge.EVENT_BUS.register(new ServerStarting(runnable));
    }

    @Override
    public void registerServerStoppingCallback(Consumer<MinecraftServer> runnable) {
        MinecraftForge.EVENT_BUS.register(new ServerStopping(runnable));
    }

    @Override
    public void registerServerStoppedCallback(Consumer<MinecraftServer> runnable) {
        MinecraftForge.EVENT_BUS.register(new ServerStopped(runnable));
    }

    private record ServerStarting(Consumer<MinecraftServer> runnable)
    {
        @SubscribeEvent
        public void onServerStarting(ServerStartingEvent event) {
            runnable.accept(event.getServer());
        }
    }

    private record ServerStopping(Consumer<MinecraftServer> runnable)
    {
        @SubscribeEvent
        public void onServerStopping(ServerStoppingEvent event) {
            runnable.accept(event.getServer());
        }
    }

    private record ServerStopped(Consumer<MinecraftServer> runnable)
    {
        @SubscribeEvent
        public void onServerStopped(ServerStoppedEvent event) {
            runnable.accept(event.getServer());
        }
    }
}
