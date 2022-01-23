package dev.the_fireplace.lib.mixin;

import dev.the_fireplace.lib.chat.translation.ModLanguageMaps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.language.LanguageManager;
import net.minecraft.resource.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LanguageManager.class)
@Environment(EnvType.CLIENT)
public class LanguageManagerMixin
{
    @Inject(at = @At("RETURN"), method = "reload")
    private void resetFireplaceLibLanguages(ResourceManager manager, CallbackInfo ci) {
        ModLanguageMaps.reloadLanguage();
    }
}
