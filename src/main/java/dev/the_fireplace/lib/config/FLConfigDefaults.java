package dev.the_fireplace.lib.config;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.domain.config.ConfigValues;

import javax.inject.Singleton;

@Implementation(name = "default")
@Singleton
public final class FLConfigDefaults implements ConfigValues
{
    @Override
    public String getLocale() {
        return "en_us";
    }

    @Override
    public short getEssentialThreadPoolSize() {
        return 16;
    }

    @Override
    public short getNonEssentialThreadPoolSize() {
        return 8;
    }
}
