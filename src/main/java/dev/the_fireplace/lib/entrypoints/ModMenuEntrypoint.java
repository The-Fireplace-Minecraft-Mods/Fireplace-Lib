package dev.the_fireplace.lib.entrypoints;

import dev.the_fireplace.annotateddi.api.DIContainer;
import dev.the_fireplace.lib.config.FLConfigScreenFactory;
import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;

@Environment(EnvType.CLIENT)
public final class ModMenuEntrypoint implements ModMenuApi
{
    private final FLConfigScreenFactory flConfigScreenFactory = DIContainer.get().getInstance(FLConfigScreenFactory.class);

    @Override
    public String getModId() {
        return FireplaceLib.MODID;
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (ConfigScreenFactory<Screen>) flConfigScreenFactory::getConfigScreen;
    }
}
