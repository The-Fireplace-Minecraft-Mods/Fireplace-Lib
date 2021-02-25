package dev.the_fireplace.lib.impl.translation.proxy;

public abstract class LocaleProxy {
    private static LocaleProxy instance = null;
    public static LocaleProxy getInstance() {
        LocaleProxy inst = instance;
        if (inst == null) {
            instance = new ServerLocaleProxy();
        }

        return instance;
    }

    public static void setLocaleProxy(LocaleProxy proxy) {
        instance = proxy;
    }

    public abstract String getLocale();
}
