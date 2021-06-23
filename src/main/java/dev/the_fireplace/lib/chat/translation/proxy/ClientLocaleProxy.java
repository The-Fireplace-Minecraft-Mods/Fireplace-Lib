package dev.the_fireplace.lib.chat.translation.proxy;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;

@Environment(EnvType.CLIENT)
public class ClientLocaleProxy extends LocaleProxy {
    @Override
    public String getLocale() {
        return MinecraftClient.getInstance().getLanguageManager().getLanguage().getCode();
    }
}
