package dev.the_fireplace.lib.config;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.domain.config.ConfigValues;

import javax.inject.Singleton;

@Implementation(name="default")
@Singleton
public class FLConfigDefaults implements ConfigValues {
    @Override
    public String getLocale() {
        return "en_us";
    }

    @Override
    public short getEssentialThreadPoolSize() {
        return 256;
    }

    @Override
    public short getNonEssentialThreadPoolSize() {
        return 128;
    }

    @Override
    public void setLocale(String locale) {

    }

    @Override
    public void setEssentialThreadPoolSize(short essentialThreadPoolSize) {

    }

    @Override
    public void setNonEssentialThreadPoolSize(short nonEssentialThreadPoolSize) {

    }
}
