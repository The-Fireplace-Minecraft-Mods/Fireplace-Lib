package dev.the_fireplace.lib.entrypoints;

import dev.the_fireplace.annotateddi.api.DIEventBus;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkConstants;

@Mod("fireplacelib")
public final class Forge
{
    public Forge() {
        // Register as optional on both sides
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));

        // Register "entrypoints"
        DIEventBus.BUS.register(new Main());
        DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> {
            DIEventBus.BUS.register(new Client());
            return null;
        });
    }
}
