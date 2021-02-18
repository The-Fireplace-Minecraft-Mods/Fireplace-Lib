package dev.the_fireplace.lib.impl.storage.access;

import dev.the_fireplace.lib.api.io.DirectoryResolver;
import dev.the_fireplace.lib.api.storage.ConfigBasedSerializable;
import dev.the_fireplace.lib.api.storage.SaveBasedSerializable;

import java.nio.file.Path;

public final class JsonStoragePath {
    private static final DirectoryResolver DIRECTORY_RESOLVER = DirectoryResolver.getInstance();
    
    static Path resolveSaveBasedJsonFilePath(SaveBasedSerializable saveBasedSerializable) {
        Path filePath = DIRECTORY_RESOLVER.getSavePath();

        if (!saveBasedSerializable.getDatabase().isEmpty()) {
            filePath = filePath.resolve(saveBasedSerializable.getDatabase());
        } else {
            throw new IllegalStateException("Storable was missing a database!");
        }
        if (!saveBasedSerializable.getTable().isEmpty()) {
            filePath = filePath.resolve(saveBasedSerializable.getTable());
        } else {
            throw new IllegalStateException("Storable was missing a table!");
        }
        if (!saveBasedSerializable.getId().isEmpty()) {
            filePath = filePath.resolve(saveBasedSerializable.getId() + ".json");
        } else {
            throw new IllegalStateException("Storable was missing an ID!");
        }

        return filePath;
    }

    static Path resolveConfigBasedJsonFilePath(ConfigBasedSerializable configBasedSerializable) {
        Path filePath = DIRECTORY_RESOLVER.getConfigPath();

        if (!configBasedSerializable.getSubfolderName().isEmpty()) {
            filePath = filePath.resolve(configBasedSerializable.getSubfolderName());
        }
        if (!configBasedSerializable.getId().isEmpty()) {
            filePath = filePath.resolve(configBasedSerializable.getId() + ".json");
        } else {
            throw new IllegalStateException("Storable was missing an ID!");
        }

        return filePath;
    }
}
