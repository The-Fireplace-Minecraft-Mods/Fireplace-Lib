package dev.the_fireplace.lib.entrypoints;

import dev.the_fireplace.lib.chat.translation.proxy.ClientLocaleProxy;
import dev.the_fireplace.lib.chat.translation.proxy.LocaleProxy;
import net.fabricmc.api.ClientModInitializer;

public final class Client implements ClientModInitializer
{
    @Override
    public void onInitializeClient() {
        LocaleProxy.setLocaleProxy(new ClientLocaleProxy());
    }
}
