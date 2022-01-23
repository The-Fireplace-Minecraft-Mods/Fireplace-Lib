package dev.the_fireplace.lib.api.io.interfaces;

public interface SaveBasedSerializable extends Readable, Writable
{
    /**
     * Database name.
     * Required to match ^[a-zA-Z0-9_\-]+$
     * Dashes will be removed and capitals converted to lowercase.
     */
    String getDatabase();

    /**
     * Table name.
     * Required to match ^[a-zA-Z0-9_\-]+$
     * Dashes will be removed and capitals converted to lowercase.
     */
    String getTable();

    /**
     * Object ID.
     * Required to match ^[a-zA-Z0-9_\-]+$
     * Dashes will be removed and capitals converted to lowercase.
     */
    String getId();
}
