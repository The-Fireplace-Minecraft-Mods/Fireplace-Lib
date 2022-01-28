package dev.the_fireplace.lib.api.client.entrypoints;

import com.google.inject.Injector;
import dev.the_fireplace.lib.api.client.interfaces.ConfigGuiRegistry;

public interface ConfigGuiEntrypoint
{
    void registerConfigGuis(Injector injector, ConfigGuiRegistry configGuiRegistry);
}
