package dev.the_fireplace.lib.entrypoints;

import com.google.inject.Injector;
import dev.the_fireplace.annotateddi.api.entrypoints.DIModInitializer;
import dev.the_fireplace.lib.init.FireplaceLibInitializer;

public final class Main implements DIModInitializer
{
    @Override
    public void onInitialize(Injector container) {
        container.getInstance(FireplaceLibInitializer.class).init();
    }
}
