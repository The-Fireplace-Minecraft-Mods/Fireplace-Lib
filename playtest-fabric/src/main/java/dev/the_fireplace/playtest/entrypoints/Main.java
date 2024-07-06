package dev.the_fireplace.playtest.entrypoints;

import dev.the_fireplace.playtest.PlaytestConstants;
import dev.the_fireplace.playtest.init.IPlaytestInitializer;
import net.fabricmc.api.ModInitializer;

public final class Main implements ModInitializer
{
    @Override
    public void onInitialize() {
        PlaytestConstants.getInjector().getInstance(IPlaytestInitializer.class).init();
    }
}
