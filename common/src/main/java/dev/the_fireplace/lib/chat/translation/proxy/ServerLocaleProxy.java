package dev.the_fireplace.lib.chat.translation.proxy;

import dev.the_fireplace.lib.domain.config.ConfigValues;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ServerLocaleProxy extends LocaleProxy
{

    private final ConfigValues values;

    @Inject
    public ServerLocaleProxy(ConfigValues values) {
        this.values = values;
    }

    @Override
    public String getLocale() {
        return values.getLocale();
    }
}
