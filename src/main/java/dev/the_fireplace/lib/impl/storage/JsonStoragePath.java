package dev.the_fireplace.lib.impl.storage;

import dev.the_fireplace.lib.api.io.DirectoryResolver;
import dev.the_fireplace.lib.api.storage.Storable;
import dev.the_fireplace.lib.api.storage.access.intermediary.StorageType;

import java.nio.file.Path;

public final class JsonStoragePath {
    private static final DirectoryResolver DIRECTORY_RESOLVER = DirectoryResolver.getInstance();
    
    private static Path getBaseStoragePath(StorageType type) {
        switch (type) {
            case SAVE:
                return DIRECTORY_RESOLVER.getSavePath();
            case CONFIG:
                return DIRECTORY_RESOLVER.getConfigPath();
            default:
                throw new IllegalArgumentException("Invalid Storage Type!");
        }
    }
    
    static Path resolveJsonFilePath(Storable storable) {
        Path filePath = getBaseStoragePath(storable.getType());

        if (!storable.getDatabase().isEmpty()) {
            filePath = filePath.resolve(storable.getDatabase());
        }
        if (!storable.getTable().isEmpty()) {
            filePath = filePath.resolve(storable.getTable());
        }
        if (!storable.getId().isEmpty()) {
            filePath = filePath.resolve(storable.getId() + ".json");
        } else {
            throw new IllegalStateException("Storable was missing an ID!");
        }

        return filePath;
    }
}
