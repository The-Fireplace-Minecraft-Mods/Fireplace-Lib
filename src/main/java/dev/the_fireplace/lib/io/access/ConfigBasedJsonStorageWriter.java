package dev.the_fireplace.lib.io.access;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.io.injectables.ConfigBasedStorageWriter;
import dev.the_fireplace.lib.api.io.interfaces.ConfigBasedSerializable;
import dev.the_fireplace.lib.api.lazyio.interfaces.Defaultable;
import dev.the_fireplace.lib.entrypoints.FireplaceLib;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

@Implementation
@Singleton
public final class ConfigBasedJsonStorageWriter implements ConfigBasedStorageWriter {
    private final Gson gson;
    private final Logger logger;
    private final JsonStoragePath jsonStoragePath;

    @Inject
    public ConfigBasedJsonStorageWriter(JsonStoragePath jsonStoragePath) {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.logger = FireplaceLib.getLogger();
        this.jsonStoragePath = jsonStoragePath;
    }

    @Override
    public boolean write(ConfigBasedSerializable writable) {
        Path filePath = jsonStoragePath.resolveConfigBasedJsonFilePath(writable);

        File folder = filePath.getParent().toFile();
        if (!folder.exists() && !folder.mkdirs()) {
            logger.error("Unable to make folder for {}!", filePath.toString());
            return false;
        }

        File outputFile = filePath.toFile();
        if (writable instanceof Defaultable defaultable && defaultable.isDefault()) {
            if (outputFile.exists() && !outputFile.delete()) {
                logger.error("Unable to delete {}!", filePath.toString());
            }

            return true;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile), Short.MAX_VALUE)) {
            JsonStorageWriteBuffer writeBuffer = new JsonStorageWriteBuffer(new JsonObject());
            writable.writeTo(writeBuffer);
            gson.toJson(writeBuffer.getObj(), bw);
            return true;
        } catch (IOException e) {
            logger.error("Failed to write file!", e);
            return false;
        }
    }

    @Override
    public boolean delete(ConfigBasedSerializable writable) {
        Path filePath = jsonStoragePath.resolveConfigBasedJsonFilePath(writable);

        File folder = filePath.getParent().toFile();
        if (!folder.exists()) {
            return false;
        }

        File outputFile = filePath.toFile();
        if (!outputFile.exists()) {
            return false;
        }

        return outputFile.delete();
    }
}
