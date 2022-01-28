package dev.the_fireplace.lib.entrypoints;

import com.google.inject.Injector;
import dev.the_fireplace.lib.api.client.entrypoints.ConfigGuiEntrypoint;
import dev.the_fireplace.lib.api.client.interfaces.ConfigGuiRegistry;
import dev.the_fireplace.lib.config.FLConfigScreenFactory;

public final class ConfigGui implements ConfigGuiEntrypoint
{
    @Override
    public void registerConfigGuis(Injector injector, ConfigGuiRegistry configGuiRegistry) {
        FLConfigScreenFactory flConfigScreenFactory = injector.getInstance(FLConfigScreenFactory.class);
        configGuiRegistry.register(flConfigScreenFactory::getConfigScreen);
    }
}
