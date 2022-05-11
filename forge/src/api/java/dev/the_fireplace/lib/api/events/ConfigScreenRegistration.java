package dev.the_fireplace.lib.api.events;

import dev.the_fireplace.lib.api.client.interfaces.ConfigGuiRegistry;
import net.minecraftforge.eventbus.api.Event;

/**
 * Event for registering a config GUI for use in the mod menu and potentially with other mods in the future.
 * Subscribe to this event on {@link FLEventBus#BUS}.
 */
public final class ConfigScreenRegistration extends Event
{
    private final ConfigGuiRegistry configGuiRegistry;

    public ConfigScreenRegistration(ConfigGuiRegistry configGuiRegistry) {
        this.configGuiRegistry = configGuiRegistry;
    }

    public ConfigGuiRegistry getConfigGuiRegistry() {
        return configGuiRegistry;
    }
}
