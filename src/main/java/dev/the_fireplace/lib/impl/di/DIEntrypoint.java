package dev.the_fireplace.lib.impl.di;

import dev.the_fireplace.annotateddi.entrypoint.DIModuleEntrypoint;
import dev.the_fireplace.shadowed.com.google.inject.AbstractModule;

import java.util.Collection;
import java.util.Set;

public final class DIEntrypoint implements DIModuleEntrypoint {
    @Override
    public Collection<AbstractModule> getModules() {
        return Set.of(new FireplaceLibDI());
    }
}
