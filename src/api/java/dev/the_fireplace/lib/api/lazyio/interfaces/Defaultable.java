package dev.the_fireplace.lib.api.lazyio.interfaces;

/**
 * Typically used to determine if a file/object should be persisted, or if it can be disposed of because it already has the defaults that will be used anyways.
 */
public interface Defaultable
{
    boolean isDefault();
}
