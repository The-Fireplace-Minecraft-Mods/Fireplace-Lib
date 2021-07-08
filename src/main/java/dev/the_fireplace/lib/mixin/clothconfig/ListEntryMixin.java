package dev.the_fireplace.lib.mixin.clothconfig;

import dev.the_fireplace.lib.config.ClothConfigDependencyHandler;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.gui.entries.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {
    TooltipListEntry.class,
    BaseListEntry.class,
    BooleanListEntry.class,
    DropdownBoxEntry.class,
    IntegerSliderEntry.class,
    LongSliderEntry.class,
    SelectionListEntry.class,
    SubCategoryListEntry.class,
    TextFieldListEntry.class,
    TextListEntry.class,
    SelectionListEntry.class,
}, remap = false)
public abstract class ListEntryMixin<T> extends AbstractConfigListEntry<T> {

    public ListEntryMixin(String fieldName, boolean requiresRestart) {
        super(fieldName, requiresRestart);
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    protected void render(int var1, int var2, int var3, int var4, int var5, int var6, int var7, boolean var8, float var9, CallbackInfo ci) {
        if (ClothConfigDependencyHandler.DISABLED_ENTRIES.contains(this)) {
            ci.cancel();
        }
    }
}
