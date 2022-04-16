package dev.the_fireplace.lib.api.events;

import net.minecraftforge.eventbus.api.BusBuilder;
import net.minecraftforge.eventbus.api.IEventBus;

public final class FLEventBus
{
    public static final IEventBus BUS = BusBuilder.builder().build();
}
