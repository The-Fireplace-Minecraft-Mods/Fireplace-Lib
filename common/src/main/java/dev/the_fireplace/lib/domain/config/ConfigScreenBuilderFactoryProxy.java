package dev.the_fireplace.lib.domain.config;

import dev.the_fireplace.lib.api.client.injectables.ConfigScreenBuilderFactory;

public interface ConfigScreenBuilderFactoryProxy
{
    void setActiveConfigScreenBuilderFactory(ConfigScreenBuilderFactory factory);

    boolean hasActiveFactory();
}
