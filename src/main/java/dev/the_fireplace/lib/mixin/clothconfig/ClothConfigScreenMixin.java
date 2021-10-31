package dev.the_fireplace.lib.mixin.clothconfig;

import me.shedaniel.clothconfig2.gui.ClothConfigScreen;
import me.shedaniel.math.Rectangle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClothConfigScreen.class)
public class ClothConfigScreenMixin
{
    @Shadow
    private Rectangle tabsRightBounds;

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void cancelRenderingPreInitialization(MatrixStack xx, int child, int onlyInnerTabBounds, float entry, CallbackInfo ci) {
        if (this.tabsRightBounds == null) {
            ci.cancel();
        }
    }
}
