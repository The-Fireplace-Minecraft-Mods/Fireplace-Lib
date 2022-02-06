package dev.the_fireplace.lib.api.client.entrypoints;

import com.google.inject.Injector;
import dev.the_fireplace.lib.api.client.interfaces.ConfigGuiRegistry;

/**
 * Registers a config GUI for use with Mod Menu and potentially other mods in the future.
 * Register with the "fireplacelib" entrypoint in fabric.mod.json
 */
public interface ConfigGuiEntrypoint
{
    void registerConfigGuis(Injector injector, ConfigGuiRegistry configGuiRegistry);
}
