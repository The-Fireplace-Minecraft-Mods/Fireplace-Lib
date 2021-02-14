package dev.the_fireplace.lib.impl.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.the_fireplace.lib.api.storage.Defaultable;
import dev.the_fireplace.lib.api.storage.Writable;
import dev.the_fireplace.lib.api.storage.access.StorageWriter;
import dev.the_fireplace.lib.impl.FireplaceLib;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public final class JsonStorageWriter implements StorageWriter {
    @Deprecated
    public static final StorageWriter INSTANCE = new JsonStorageWriter();

    private final Gson gson;
    private final Logger logger;

    private JsonStorageWriter() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        logger = FireplaceLib.getLogger();
    }

    @Override
    public boolean write(Writable writable) {
        Path filePath = JsonStoragePath.resolveJsonFilePath(writable);

        File folder = filePath.getParent().toFile();
        if (!folder.exists() && !folder.mkdirs()) {
            logger.error("Unable to make folder for {}!", filePath.toString());
            return false;
        }

        File outputFile = filePath.toFile();
        if (writable instanceof Defaultable && ((Defaultable) writable).isDefault()) {
            if (outputFile.exists() && !outputFile.delete()) {
                logger.error("Unable to delete {}!", filePath.toString());
            }

            return true;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile), Short.MAX_VALUE)) {
            gson.toJson(writable.toJson(), bw);
            return true;
        } catch (IOException e) {
            logger.error("Failed to write file!", e);
            return false;
        }
    }
}
