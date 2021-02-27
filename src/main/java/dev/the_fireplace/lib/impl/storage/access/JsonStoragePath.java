package dev.the_fireplace.lib.impl.storage.access;

import dev.the_fireplace.lib.api.io.DirectoryResolver;
import dev.the_fireplace.lib.api.storage.ConfigBasedSerializable;
import dev.the_fireplace.lib.api.storage.SaveBasedSerializable;

import java.nio.file.Path;

public final class JsonStoragePath {
    private static final DirectoryResolver DIRECTORY_RESOLVER = DirectoryResolver.getInstance();
    
    static Path resolveSaveBasedJsonFilePath(SaveBasedSerializable saveBasedSerializable) {
        return resolveSaveBasedJsonFilePath(saveBasedSerializable.getDatabase(), saveBasedSerializable.getTable(), saveBasedSerializable.getId());
    }

    static Path resolveSaveBasedJsonFilePath(String database, String table, String id) {
        Path filePath = resolveSaveBasedJsonDirectory(database, table);
        if (!id.isEmpty() && SchemaValidator.isValid(id)) {
            return filePath.resolve(SchemaValidator.minimizeSchema(id) + ".json");
        } else {
            throw new IllegalStateException("Invalid storable ID!");
        }
    }

    static Path resolveSaveBasedJsonDirectory(String database, String table) {
        Path filePath = DIRECTORY_RESOLVER.getSavePath();

        if (!database.isEmpty() && SchemaValidator.isValid(database)) {
            filePath = filePath.resolve(SchemaValidator.minimizeSchema(database));
        } else {
            throw new IllegalStateException("Invalid storable database!");
        }
        if (!table.isEmpty() && SchemaValidator.isValid(table)) {
            filePath = filePath.resolve(SchemaValidator.minimizeSchema(table));
        } else {
            throw new IllegalStateException("Invalid storable table!");
        }
        return filePath;
    }

    static Path resolveConfigBasedJsonFilePath(ConfigBasedSerializable configBasedSerializable) {
        Path filePath = DIRECTORY_RESOLVER.getConfigPath();

        String subfolder = SchemaValidator.minimizeSchema(configBasedSerializable.getSubfolderName());
        if (!subfolder.isEmpty() && SchemaValidator.isValid(subfolder)) {
            filePath = filePath.resolve(subfolder);
        }
        if (!configBasedSerializable.getId().isEmpty() && SchemaValidator.isValid(configBasedSerializable.getId())) {
            filePath = filePath.resolve(SchemaValidator.minimizeSchema(configBasedSerializable.getId()) + ".json");
        } else {
            throw new IllegalStateException("Invalid storable ID!");
        }

        return filePath;
    }
}
