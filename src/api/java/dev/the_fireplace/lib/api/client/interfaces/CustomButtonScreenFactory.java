package dev.the_fireplace.lib.api.client.interfaces;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;

@Environment(EnvType.CLIENT)
public interface CustomButtonScreenFactory<S> {
    <T extends Screen & CustomButtonScreen<S>> T createScreen(Screen parent, S currentValue);
}
