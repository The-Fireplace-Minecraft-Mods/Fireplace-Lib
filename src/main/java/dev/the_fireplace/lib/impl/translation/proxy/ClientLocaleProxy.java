package dev.the_fireplace.lib.impl.translation.proxy;

import net.minecraft.client.MinecraftClient;

public class ClientLocaleProxy extends LocaleProxy {
    @Override
    public String getLocale() {
        return MinecraftClient.getInstance().getLanguageManager().getLanguage().getCode();
    }
}
