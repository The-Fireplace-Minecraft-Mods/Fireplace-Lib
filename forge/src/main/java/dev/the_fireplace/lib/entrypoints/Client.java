package dev.the_fireplace.lib.entrypoints;

import com.google.inject.Injector;
import dev.the_fireplace.annotateddi.api.events.InjectorInitialized;
import dev.the_fireplace.lib.api.events.FLEventBus;
import dev.the_fireplace.lib.chat.translation.proxy.ClientLocaleProxy;
import dev.the_fireplace.lib.chat.translation.proxy.LocaleProxy;
import dev.the_fireplace.lib.config.ForgeConfigScreenLoader;
import dev.the_fireplace.lib.events.ConfigGuiRegistryEventHandler;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

final class Client
{
    @SubscribeEvent
    public void onInjectorInitialized(InjectorInitialized.Client event) {
        LocaleProxy.setLocaleProxy(new ClientLocaleProxy());
        Injector injector = event.getInjector();

        FMLJavaModLoadingContext.get().getModEventBus().register(injector.getInstance(ForgeConfigScreenLoader.class));
        FLEventBus.BUS.register(injector.getInstance(ConfigGuiRegistryEventHandler.class));
    }
}
