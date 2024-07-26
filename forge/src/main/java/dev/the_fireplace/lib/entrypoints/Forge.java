package dev.the_fireplace.lib.entrypoints;

import com.google.inject.Injector;
import dev.the_fireplace.lib.FireplaceLibConstants;
import dev.the_fireplace.lib.init.FireplaceLibInitializer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

@Mod("fireplacelib")
public final class Forge
{
    public Forge() {
        Injector injector = FireplaceLibConstants.getInjector();
        injector.getInstance(FireplaceLibInitializer.class).init();
        DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> {
            injector.getInstance(ForgeClientInitializer.class).init();
            return null;
        });

        // Register as optional on both sides
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> IExtensionPoint.DisplayTest.IGNORESERVERONLY, (a, b) -> true));
    }
}
