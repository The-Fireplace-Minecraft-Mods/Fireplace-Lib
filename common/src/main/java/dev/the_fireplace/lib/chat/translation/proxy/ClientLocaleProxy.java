package dev.the_fireplace.lib.chat.translation.proxy;

import net.minecraft.client.Minecraft;

public class ClientLocaleProxy extends LocaleProxy
{
    @Override
    public String getLocale() {
        return Minecraft.getInstance().getLanguageManager().getSelected().getCode();
    }
}
