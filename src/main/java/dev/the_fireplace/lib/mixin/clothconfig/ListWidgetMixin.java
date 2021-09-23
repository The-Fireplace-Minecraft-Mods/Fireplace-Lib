package dev.the_fireplace.lib.mixin.clothconfig;

import dev.the_fireplace.lib.config.cloth.ClothConfigDependencyHandler;
import me.shedaniel.clothconfig2.api.AbstractConfigEntry;
import me.shedaniel.clothconfig2.gui.ClothConfigScreen;
import me.shedaniel.clothconfig2.gui.widget.DynamicElementListWidget;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ClothConfigScreen.ListWidget.class, remap = false)
public final class ListWidgetMixin<R extends DynamicElementListWidget.ElementEntry<R>> {

    @Inject(method = "renderItem", at = @At("HEAD"), cancellable = true)
    protected void renderItem(MatrixStack matrices, R item, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isSelected, float delta, CallbackInfo ci) {
        if (item instanceof AbstractConfigEntry<?> configEntry && ClothConfigDependencyHandler.DISABLED_ENTRIES.contains(configEntry)) {
            ci.cancel();
        }
    }
}
