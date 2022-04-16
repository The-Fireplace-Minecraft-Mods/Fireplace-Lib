package dev.the_fireplace.lib.mixin.clothconfig;

import me.shedaniel.clothconfig2.gui.AbstractConfigScreen;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = AbstractConfigScreen.class, remap = false)
public interface AbstractConfigScreenAccessor
{
    @Accessor
    Screen getParent();
}
