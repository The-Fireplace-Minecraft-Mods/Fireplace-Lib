package dev.the_fireplace.playtest.entrypoints;

import dev.the_fireplace.playtest.PlaytestConstants;
import dev.the_fireplace.playtest.init.IPlaytestInitializer;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkConstants;

@Mod(PlaytestConstants.MODID)
public final class Forge
{
    public Forge() {
        PlaytestConstants.getInjector().getInstance(IPlaytestInitializer.class).init();

        // Register as optional on both sides
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));
    }
}

