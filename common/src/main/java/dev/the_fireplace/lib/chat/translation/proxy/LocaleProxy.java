package dev.the_fireplace.lib.chat.translation.proxy;

import dev.the_fireplace.lib.FireplaceLibConstants;

public abstract class LocaleProxy
{
    private static LocaleProxy instance = null;

    public static LocaleProxy getInstance() {
        LocaleProxy inst = instance;
        if (inst == null) {
            instance = FireplaceLibConstants.getInjector().getInstance(ServerLocaleProxy.class);
        }

        return instance;
    }

    public static void setLocaleProxy(LocaleProxy proxy) {
        instance = proxy;
    }

    public abstract String getLocale();
}
