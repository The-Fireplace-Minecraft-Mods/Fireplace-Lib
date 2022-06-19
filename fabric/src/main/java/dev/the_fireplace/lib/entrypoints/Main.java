package dev.the_fireplace.lib.entrypoints;

import dev.the_fireplace.lib.FireplaceLibConstants;
import dev.the_fireplace.lib.init.FireplaceLibInitializer;
import net.fabricmc.api.ModInitializer;

public final class Main implements ModInitializer
{
    @Override
    public void onInitialize() {
        FireplaceLibConstants.getInjector().getInstance(FireplaceLibInitializer.class).init();
    }
}
