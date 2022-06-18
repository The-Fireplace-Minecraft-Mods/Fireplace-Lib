package dev.the_fireplace.lib.environment;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.environment.injectables.EnvironmentData;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

@Implementation
public final class FabricEnvironmentData implements EnvironmentData
{
    @Override
    public boolean isDevelopment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public boolean isClient() {
        return FabricLoader.getInstance().getEnvironmentType().equals(EnvType.CLIENT);
    }

    @Override
    public boolean isDedicatedServer() {
        return FabricLoader.getInstance().getEnvironmentType().equals(EnvType.SERVER);
    }
}
