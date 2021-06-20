package dev.the_fireplace.lib.impl.di;

import com.google.inject.AbstractModule;
import dev.the_fireplace.lib.api.io.injectables.FilePathStorage;
import dev.the_fireplace.lib.impl.io.LazyFilePathMemory;

public final class FireplaceLibDI extends AbstractModule {
    @Override
    protected void configure() {
        bind(FilePathStorage.class).toProvider(() -> {
            //noinspection deprecation
            return LazyFilePathMemory.ACCESS;
        });
    }
}
