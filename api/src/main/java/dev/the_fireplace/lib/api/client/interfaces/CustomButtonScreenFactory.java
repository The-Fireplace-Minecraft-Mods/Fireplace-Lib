package dev.the_fireplace.lib.api.client.interfaces;


import net.minecraft.client.gui.screens.Screen;

/**
 * Client side only.
 */
public interface CustomButtonScreenFactory<S, T extends Screen & CustomButtonScreen<S>>
{
    T createScreen(Screen parent, S currentValue);
}
