package dev.the_fireplace.lib.mixin.clothconfig;

import me.shedaniel.clothconfig2.gui.ClothConfigScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClothConfigScreen.class)
public abstract class ClothConfigScreenMixin
{
    private boolean hasFinishedInitialization = false;

    @Inject(method = "init", at = @At("RETURN"))
    public void markFinishedInitialization(CallbackInfo ci) {
        this.hasFinishedInitialization = true;
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void cancelRenderingBeforeInitializationFinishes(int child, int onlyInnerTabBounds, float entry, CallbackInfo ci) {
        if (!this.hasFinishedInitialization) {
            ci.cancel();
        }
    }
}
