package dev.the_fireplace.lib.entrypoints;

import dev.the_fireplace.annotateddi.api.events.InjectorInitialized;
import dev.the_fireplace.lib.chat.translation.proxy.ClientLocaleProxy;
import dev.the_fireplace.lib.chat.translation.proxy.LocaleProxy;
import net.minecraftforge.eventbus.api.SubscribeEvent;

final class Client
{
    @SubscribeEvent
    public void onInjectorInitialized(InjectorInitialized.Client event) {
        LocaleProxy.setLocaleProxy(new ClientLocaleProxy());
    }
}
