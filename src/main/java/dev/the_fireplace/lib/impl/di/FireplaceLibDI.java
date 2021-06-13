package dev.the_fireplace.lib.impl.di;

import dev.the_fireplace.lib.api.io.FilePathStorage;
import dev.the_fireplace.lib.impl.io.LazyFilePathMemory;
import dev.the_fireplace.shadowed.com.google.inject.AbstractModule;

public final class FireplaceLibDI extends AbstractModule {
    @Override
    protected void configure() {
        bind(FilePathStorage.class).toProvider(() -> {
            //noinspection deprecation
            return LazyFilePathMemory.ACCESS;
        });
    }
}
