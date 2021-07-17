package dev.the_fireplace.lib.api.lazyio.interfaces;

import dev.the_fireplace.lib.api.io.interfaces.ConfigBasedSerializable;
import dev.the_fireplace.lib.api.io.interfaces.access.SimpleBuffer;

public interface Config extends ConfigBasedSerializable {
    /**
     * Called only when the config is reloaded - not when it first loads
     * @param changedValues
     *  A buffer containing only the keys with differences after reloading, and their previous values
     */
    default void afterReload(SimpleBuffer changedValues) {}
}
