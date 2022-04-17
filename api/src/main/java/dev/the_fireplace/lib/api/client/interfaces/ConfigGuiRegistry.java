package dev.the_fireplace.lib.api.client.interfaces;

import net.minecraft.client.gui.screens.Screen;

/**
 * Client side only
 */
public interface ConfigGuiRegistry
{
    <S extends Screen> void register(ConfigScreenFactory<S> createConfigGui);
}
