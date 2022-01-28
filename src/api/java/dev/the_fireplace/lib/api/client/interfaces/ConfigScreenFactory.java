package dev.the_fireplace.lib.api.client.interfaces;

import net.minecraft.client.gui.screen.Screen;

@FunctionalInterface
public interface ConfigScreenFactory<S extends Screen>
{
    S create(Screen parent);
}
