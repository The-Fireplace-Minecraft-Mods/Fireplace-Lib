package dev.the_fireplace.lib.io.access;

import dev.the_fireplace.lib.api.io.injectables.DirectoryResolver;
import dev.the_fireplace.lib.api.io.interfaces.SaveBasedSerializable;
import net.minecraft.resources.ResourceLocation;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.nio.file.Path;

@Singleton
public final class JsonStoragePath
{
    private final DirectoryResolver directoryResolver;

    @Inject
    public JsonStoragePath(DirectoryResolver directoryResolver) {
        this.directoryResolver = directoryResolver;
    }

    Path resolveSaveBasedJsonFilePath(SaveBasedSerializable saveBasedSerializable) {
        return resolveSaveBasedJsonFilePath(saveBasedSerializable.getDatabase(), saveBasedSerializable.getTable(), saveBasedSerializable.getId());
    }

    Path resolveSaveBasedJsonFilePath(String database, String table, String id) {
        Path filePath = resolveSaveBasedJsonDirectory(database, table);
        if (!id.isEmpty() && SchemaValidator.isValid(id)) {
            return filePath.resolve(SchemaValidator.minimizeSchema(id) + ".json");
        } else {
            throw new IllegalStateException("Invalid storable ID!");
        }
    }

    Path resolveSaveBasedJsonDirectory(String database, String table) {
        Path filePath = directoryResolver.getSavePath();

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

    public Path resolveConfigBasedJsonFilePath(String domain, String id) {
        Path filePath = resolveConfigSubfolderPath(domain);
        if (!id.isEmpty() && SchemaValidator.isValid(id)) {
            filePath = filePath.resolve(SchemaValidator.minimizeSchema(id) + ".json");
        } else {
            throw new IllegalStateException("Invalid storable ID!");
        }

        return filePath;
    }

    Path resolveConfigSubfolderPath(String subfolder) {
        Path filePath = directoryResolver.getConfigPath();

        subfolder = SchemaValidator.minimizeSchema(subfolder);
        if (!subfolder.isEmpty() && SchemaValidator.isValid(subfolder)) {
            filePath = filePath.resolve(subfolder);
        }
        return filePath;
    }

    public Path resolveConfigBasedJsonFilePath(String domain, ResourceLocation id) {
        Path filePath = resolveConfigSubfolderPath(domain);
        String idNamespace = SchemaValidator.minimizeSchema(id.getNamespace());
        if (!idNamespace.isEmpty() && SchemaValidator.isValid(idNamespace)) {
            filePath = filePath.resolve(idNamespace);
        }
        String idPath = id.getPath();
        if (!idPath.isEmpty() && SchemaValidator.isValid(idPath)) {
            filePath = filePath.resolve(SchemaValidator.minimizeSchema(idPath) + ".json");
        } else {
            throw new IllegalStateException("Invalid storable ID!");
        }

        return filePath;
    }
}
