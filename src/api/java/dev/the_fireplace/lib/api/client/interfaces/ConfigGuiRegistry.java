package dev.the_fireplace.lib.api.client.interfaces;

import net.minecraft.client.gui.screen.Screen;

public interface ConfigGuiRegistry
{
    <S extends Screen> void register(ConfigScreenFactory<S> createConfigGui);
}
