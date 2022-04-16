package dev.the_fireplace.lib.io.access;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.FireplaceLibConstants;
import dev.the_fireplace.lib.api.io.injectables.ConfigBasedStorageWriter;
import dev.the_fireplace.lib.api.io.interfaces.ConfigBasedSerializable;
import dev.the_fireplace.lib.api.io.interfaces.Writable;
import dev.the_fireplace.lib.api.lazyio.interfaces.Defaultable;
import dev.the_fireplace.lib.api.lazyio.interfaces.HierarchicalConfig;
import dev.the_fireplace.lib.domain.io.HierarchicalConfigWriter;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

@Singleton
@Implementation(allInterfaces = true)
public final class ConfigBasedJsonStorageWriter implements ConfigBasedStorageWriter, HierarchicalConfigWriter
{
    private final Gson gson;
    private final Logger logger;
    private final JsonStoragePath jsonStoragePath;

    @Inject
    public ConfigBasedJsonStorageWriter(JsonStoragePath jsonStoragePath) {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.logger = FireplaceLibConstants.getLogger();
        this.jsonStoragePath = jsonStoragePath;
    }

    @Override
    public boolean write(ConfigBasedSerializable writable) {
        String domain = writable.getSubfolderName();
        String id = writable.getId();

        return write(writable, domain, id);
    }

    @Override
    public boolean write(HierarchicalConfig writable, String domain, String id) {
        return write((Writable) writable, domain, id);
    }

    @Override
    public boolean write(HierarchicalConfig writable, String domain, ResourceLocation id) {
        return write((Writable) writable, domain, id);
    }

    private boolean write(Writable writable, String domain, String id) {
        Path filePath = jsonStoragePath.resolveConfigBasedJsonFilePath(domain, id);

        return write(writable, filePath);
    }

    private boolean write(Writable writable, String domain, ResourceLocation id) {
        Path filePath = jsonStoragePath.resolveConfigBasedJsonFilePath(domain, id);

        return write(writable, filePath);
    }

    private boolean write(Writable writable, Path filePath) {
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
            JsonStorageWriteBuffer writeBuffer = new JsonStorageWriteBuffer();
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
        String domain = writable.getSubfolderName();
        String id = writable.getId();

        return delete(domain, id);
    }

    @Override
    public boolean delete(String domain, String id) {
        Path filePath = jsonStoragePath.resolveConfigBasedJsonFilePath(domain, id);

        return delete(filePath);
    }

    @Override
    public boolean delete(String domain, ResourceLocation id) {
        Path filePath = jsonStoragePath.resolveConfigBasedJsonFilePath(domain, id);

        return delete(filePath);
    }

    private boolean delete(Path filePath) {
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
