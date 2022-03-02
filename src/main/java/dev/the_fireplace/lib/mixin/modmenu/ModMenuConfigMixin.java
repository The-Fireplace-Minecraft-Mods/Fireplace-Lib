package dev.the_fireplace.lib.mixin.modmenu;

import dev.the_fireplace.lib.FireplaceLibConstants;
import io.github.prospector.modmenu.config.ModMenuConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ModMenuConfig.class, remap = false)
@Environment(EnvType.CLIENT)
public class ModMenuConfigMixin
{
    @Shadow
    private ModMenuConfig.Sorting sorting;

    @Inject(at = @At("HEAD"), method = "getSorting", cancellable = true)
    private void preventCrashWhenSwitchingBetweenMajorVersions(CallbackInfoReturnable<ModMenuConfig.Sorting> cir) {
        if (FireplaceLibConstants.isDevelopmentEnvironment() && sorting == null) {
            cir.setReturnValue(ModMenuConfig.Sorting.ASCENDING);
        }
    }
}
