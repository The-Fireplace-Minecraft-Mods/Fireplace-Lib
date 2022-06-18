package dev.the_fireplace.lib.api.environment.injectables;

public interface EnvironmentData
{
    boolean isDevelopment();

    boolean isClient();

    boolean isDedicatedServer();
}
