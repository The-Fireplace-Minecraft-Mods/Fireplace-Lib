package dev.the_fireplace.lib.events;

import dev.the_fireplace.lib.FireplaceLibConstants;
import dev.the_fireplace.lib.api.events.ConfigScreenRegistration;
import dev.the_fireplace.lib.config.FLConfigScreenFactory;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class ConfigGuiRegistryEventHandler
{
    private final FLConfigScreenFactory flConfigScreenFactory;

    @Inject
    public ConfigGuiRegistryEventHandler(FLConfigScreenFactory flConfigScreenFactory) {
        this.flConfigScreenFactory = flConfigScreenFactory;
    }

    @SubscribeEvent
    public void onConfigScreenRegistration(ConfigScreenRegistration event) {
        event.getConfigGuiRegistry().register(FireplaceLibConstants.MODID, flConfigScreenFactory::getConfigScreen);
    }
}
