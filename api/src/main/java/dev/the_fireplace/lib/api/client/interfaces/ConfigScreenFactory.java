package dev.the_fireplace.lib.api.client.interfaces;

import net.minecraft.client.gui.screens.Screen;

/**
 * Client side only
 */
@FunctionalInterface
public interface ConfigScreenFactory<S extends Screen>
{
    S create(Screen parent);
}
