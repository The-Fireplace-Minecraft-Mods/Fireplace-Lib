package dev.the_fireplace.lib.entrypoints;

import dev.the_fireplace.annotateddi.api.DIContainer;
import dev.the_fireplace.lib.FireplaceLibConstants;
import dev.the_fireplace.lib.config.FLConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
public final class ModMenu implements ModMenuApi
{
    private final FLConfigScreenFactory flConfigScreenFactory = DIContainer.get().getInstance(FLConfigScreenFactory.class);

    @Override
    public String getModId() {
        return FireplaceLibConstants.MODID;
    }

    @Override
    public Function<Screen, ? extends Screen> getConfigScreenFactory() {
        return flConfigScreenFactory::getConfigScreen;
    }
}
