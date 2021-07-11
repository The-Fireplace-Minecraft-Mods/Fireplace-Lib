package dev.the_fireplace.lib.config;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.io.interfaces.access.StorageReadBuffer;
import dev.the_fireplace.lib.api.io.interfaces.access.StorageWriteBuffer;
import dev.the_fireplace.lib.api.lazyio.injectables.ConfigStateManager;
import dev.the_fireplace.lib.api.lazyio.interfaces.Config;
import dev.the_fireplace.lib.domain.config.ConfigValues;
import dev.the_fireplace.lib.entrypoints.FireplaceLib;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Implementation("dev.the_fireplace.lib.domain.config.ConfigValues")
@Singleton
public final class FLConfig implements Config, ConfigValues {
    private final ConfigValues defaultConfig;

    private String locale;
    private short essentialThreadPoolSize;
    private short nonEssentialThreadPoolSize;

    @Inject
    public FLConfig(ConfigStateManager configStateManager, @Named("default") ConfigValues defaultConfig) {
        this.defaultConfig = defaultConfig;
        configStateManager.initialize(this);
    }

    @Override
    public void readFrom(StorageReadBuffer buffer) {
        locale = buffer.readString("locale", defaultConfig.getLocale());
        essentialThreadPoolSize = buffer.readShort("essentialThreadPoolSize", defaultConfig.getEssentialThreadPoolSize());
        nonEssentialThreadPoolSize = buffer.readShort("nonEssentialThreadPoolSize", defaultConfig.getNonEssentialThreadPoolSize());
    }

    @Override
    public void writeTo(StorageWriteBuffer buffer) {
        buffer.writeString("locale", locale);
        buffer.writeShort("essentialThreadPoolSize", essentialThreadPoolSize);
        buffer.writeShort("nonEssentialThreadPoolSize", nonEssentialThreadPoolSize);
    }

    @Override
    public String getId() {
        return FireplaceLib.MODID;
    }

    @Override
    public String getLocale() {
        return locale;
    }

    @Override
    public short getEssentialThreadPoolSize() {
        return essentialThreadPoolSize;
    }

    @Override
    public short getNonEssentialThreadPoolSize() {
        return nonEssentialThreadPoolSize;
    }

    @Override
    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public void setEssentialThreadPoolSize(short essentialThreadPoolSize) {
        this.essentialThreadPoolSize = essentialThreadPoolSize;
    }

    @Override
    public void setNonEssentialThreadPoolSize(short nonEssentialThreadPoolSize) {
        this.nonEssentialThreadPoolSize = nonEssentialThreadPoolSize;
    }
}
