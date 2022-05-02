package dev.the_fireplace.lib.entrypoints;

import dev.the_fireplace.annotateddi.api.events.InjectorInitialized;
import dev.the_fireplace.lib.init.FireplaceLibInitializer;
import net.minecraftforge.eventbus.api.SubscribeEvent;

final class Main
{
    @SubscribeEvent
    public void onInjectorInitialized(InjectorInitialized event) {
        event.getInjector().getInstance(FireplaceLibInitializer.class).init();
    }
}
