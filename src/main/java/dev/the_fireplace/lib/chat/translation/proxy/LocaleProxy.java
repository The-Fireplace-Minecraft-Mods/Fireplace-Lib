package dev.the_fireplace.lib.chat.translation.proxy;

import dev.the_fireplace.annotateddi.api.DIContainer;

public abstract class LocaleProxy {
    private static LocaleProxy instance = null;
    public static LocaleProxy getInstance() {
        LocaleProxy inst = instance;
        if (inst == null) {
            instance = DIContainer.get().getInstance(ServerLocaleProxy.class);
        }

        return instance;
    }

    public static void setLocaleProxy(LocaleProxy proxy) {
        instance = proxy;
    }

    public abstract String getLocale();
}
