package dev.the_fireplace.lib.impl.di;

import com.google.inject.AbstractModule;
import dev.the_fireplace.annotateddi.api.entrypoints.DIModuleCreator;

import java.util.Collection;
import java.util.Set;

public final class DIEntrypoint implements DIModuleCreator {
    @Override
    public Collection<AbstractModule> getModules() {
        return Set.of(new FireplaceLibDI());
    }
}
