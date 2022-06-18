package dev.the_fireplace.lib.entrypoints;

import dev.the_fireplace.lib.FireplaceLibConstants;
import dev.the_fireplace.lib.api.client.entrypoints.ConfigGuiEntrypoint;
import dev.the_fireplace.lib.api.client.interfaces.ConfigGuiRegistry;
import dev.the_fireplace.lib.config.FLConfigScreenFactory;

public final class ConfigGui implements ConfigGuiEntrypoint
{
    @Override
    public void registerConfigGuis(ConfigGuiRegistry configGuiRegistry) {
        FLConfigScreenFactory flConfigScreenFactory = FireplaceLibConstants.getInjector().getInstance(FLConfigScreenFactory.class);
        configGuiRegistry.register(FireplaceLibConstants.MODID, flConfigScreenFactory::getConfigScreen);
    }
}
