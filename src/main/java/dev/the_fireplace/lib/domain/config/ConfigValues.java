package dev.the_fireplace.lib.domain.config;

public interface ConfigValues
{
    String getLocale();

    short getEssentialThreadPoolSize();

    short getNonEssentialThreadPoolSize();
}
