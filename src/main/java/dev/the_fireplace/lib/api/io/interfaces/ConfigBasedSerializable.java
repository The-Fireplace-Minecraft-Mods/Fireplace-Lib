package dev.the_fireplace.lib.api.io.interfaces;

public interface ConfigBasedSerializable extends Readable, Writable {

    /**
     * Subfolder name. If not empty, this is used to make a folder within the config folder.
     * Required to match ^[a-zA-Z0-9_\-]+$
     * Dashes will be removed and capitals converted to lowercase.
     */
    default String getSubfolderName() {
        return "";
    }
    /**
     * Config ID. This is used to determine the file name before the extension.
     * Required to match ^[a-zA-Z0-9_\-]+$
     * Dashes will be removed and capitals converted to lowercase.
     */
    String getId();
}
