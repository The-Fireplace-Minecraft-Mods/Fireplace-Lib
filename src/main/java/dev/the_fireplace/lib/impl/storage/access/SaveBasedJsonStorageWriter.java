package dev.the_fireplace.lib.impl.storage.access;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dev.the_fireplace.lib.api.storage.Defaultable;
import dev.the_fireplace.lib.api.storage.SaveBasedSerializable;
import dev.the_fireplace.lib.api.storage.access.SaveBasedStorageWriter;
import dev.the_fireplace.lib.impl.FireplaceLib;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public final class SaveBasedJsonStorageWriter implements SaveBasedStorageWriter {
    @Deprecated
    public static final SaveBasedStorageWriter INSTANCE = new SaveBasedJsonStorageWriter();

    private final Gson gson;
    private final Logger logger;

    private SaveBasedJsonStorageWriter() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        logger = FireplaceLib.getLogger();
    }

    @Override
    public boolean write(SaveBasedSerializable writable) {
        Path filePath = JsonStoragePath.resolveSaveBasedJsonFilePath(writable);

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
            JsonStorageWriteBuffer writeBuffer = new JsonStorageWriteBuffer(new JsonObject());
            writable.writeTo(writeBuffer);
            gson.toJson(writeBuffer.getObj(), bw);
            return true;
        } catch (IOException e) {
            logger.error("Failed to write file!", e);
            return false;
        }
    }
}
