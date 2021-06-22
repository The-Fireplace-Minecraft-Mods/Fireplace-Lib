package dev.the_fireplace.lib.translation.proxy;

import dev.the_fireplace.lib.domain.config.ConfigValues;

import javax.inject.Singleton;

@Singleton
public class ServerLocaleProxy extends LocaleProxy {

    private final ConfigValues values;

    public ServerLocaleProxy(ConfigValues values) {
        this.values = values;
    }

    @Override
    public String getLocale() {
        return values.getLocale();
    }
}
