package dev.the_fireplace.lib.init;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.lifecycle.injectables.ServerLifecycle;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fmlserverevents.FMLServerStartingEvent;
import net.minecraftforge.fmlserverevents.FMLServerStoppedEvent;
import net.minecraftforge.fmlserverevents.FMLServerStoppingEvent;

import java.util.function.Consumer;

@Implementation
public final class ForgeSpecificInitialization implements ServerLifecycle
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
        public void onServerStarting(FMLServerStartingEvent event) {
            runnable.accept(event.getServer());
        }
    }

    private record ServerStopping(Consumer<MinecraftServer> runnable)
    {
        @SubscribeEvent
        public void onServerStopping(FMLServerStoppingEvent event) {
            runnable.accept(event.getServer());
        }
    }

    private record ServerStopped(Consumer<MinecraftServer> runnable)
    {
        @SubscribeEvent
        public void onServerStopped(FMLServerStoppedEvent event) {
            runnable.accept(event.getServer());
        }
    }
}
