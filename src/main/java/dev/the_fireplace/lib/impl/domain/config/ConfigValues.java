package dev.the_fireplace.lib.impl.domain.config;

public interface ConfigValues {
    String getLocale();

    short getEssentialThreadPoolSize();

    short getNonEssentialThreadPoolSize();

    void setLocale(String locale);

    void setEssentialThreadPoolSize(short essentialThreadPoolSize);

    void setNonEssentialThreadPoolSize(short nonEssentialThreadPoolSize);
}
