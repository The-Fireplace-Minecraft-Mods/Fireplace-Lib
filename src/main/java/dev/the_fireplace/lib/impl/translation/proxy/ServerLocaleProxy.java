package dev.the_fireplace.lib.impl.translation.proxy;

import dev.the_fireplace.lib.impl.config.FLConfig;

public class ServerLocaleProxy extends LocaleProxy {
    @Override
    public String getLocale() {
        return FLConfig.getData().getLocale();
    }
}
