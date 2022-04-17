package dev.the_fireplace.lib.mixin;

import dev.the_fireplace.lib.chat.translation.ModLanguageMaps;
import net.minecraft.client.resources.language.LanguageManager;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Client side only
 */
@Mixin(LanguageManager.class)
public final class LanguageManagerMixin
{
    @Inject(at = @At("RETURN"), method = "onResourceManagerReload")
    private void resetFireplaceLibLanguages(ResourceManager manager, CallbackInfo ci) {
        ModLanguageMaps.reloadLanguage();
    }
}
