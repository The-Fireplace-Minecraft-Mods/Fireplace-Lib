package dev.the_fireplace.lib.config;

import dev.the_fireplace.lib.api.client.interfaces.ConfigGuiRegistry;
import dev.the_fireplace.lib.api.client.interfaces.ConfigScreenFactory;
import dev.the_fireplace.lib.api.events.ConfigScreenRegistration;
import dev.the_fireplace.lib.api.events.FLEventBus;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public final class ForgeConfigScreenLoader
{
    @SubscribeEvent
    public void onClientLoaded(FMLLoadCompleteEvent event) {
        ConfigGuiRegistry configGuiRegistry = new ForgeConfigGuiRegistry();
        FLEventBus.BUS.post(new ConfigScreenRegistration(configGuiRegistry));
    }

    private static final class ForgeConfigGuiRegistry implements ConfigGuiRegistry
    {
        @Override
        public <S extends Screen> void register(String modid, ConfigScreenFactory<S> createConfigGui) {
            Optional<? extends ModContainer> modContainer = ModList.get().getModContainerById(modid);
            if (modContainer.isPresent()) {
                modContainer.get().registerExtensionPoint(
                    ConfigScreenHandler.ConfigScreenFactory.class,
                    () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (minecraft, parentScreen) -> createConfigGui.create(parentScreen)
                    )
                );
            } else {
                throw new IllegalStateException("Mod with id " + modid + " is not loaded!");
            }
        }
    }
}
