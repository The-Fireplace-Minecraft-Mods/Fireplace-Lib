package dev.the_fireplace.lib.impl;

import dev.the_fireplace.lib.impl.translation.proxy.ClientLocaleProxy;
import dev.the_fireplace.lib.impl.translation.proxy.LocaleProxy;
import net.fabricmc.api.ClientModInitializer;

public class FireplaceLibClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        LocaleProxy.setLocaleProxy(new ClientLocaleProxy());
    }
}
