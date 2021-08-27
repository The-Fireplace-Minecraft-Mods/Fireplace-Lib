package dev.the_fireplace.lib.io.access;

import com.google.gson.JsonObject;
import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.io.injectables.ConfigBasedStorageReader;
import dev.the_fireplace.lib.api.io.injectables.JsonFileReader;
import dev.the_fireplace.lib.api.io.interfaces.ConfigBasedSerializable;
import dev.the_fireplace.lib.api.io.interfaces.Readable;
import dev.the_fireplace.lib.api.io.interfaces.access.StorageReadBuffer;
import dev.the_fireplace.lib.api.lazyio.interfaces.HierarchicalConfig;
import dev.the_fireplace.lib.domain.io.HierarchicalConfigReader;
import net.minecraft.util.Identifier;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.nio.file.Path;

@Singleton
@Implementation({
    "dev.the_fireplace.lib.api.io.injectables.ConfigBasedStorageReader",
    "dev.the_fireplace.lib.domain.io.HierarchicalConfigReader",
})
public final class ConfigBasedJsonStorageReader implements ConfigBasedStorageReader, HierarchicalConfigReader {
    private final JsonFileReader fileReader;
    private final JsonStoragePath jsonStoragePath;

    @Inject
    public ConfigBasedJsonStorageReader(JsonFileReader jsonFileReader, JsonStoragePath jsonStoragePath) {
        this.fileReader = jsonFileReader;
        this.jsonStoragePath = jsonStoragePath;
    }

    @Override
    public void readTo(ConfigBasedSerializable readable) {
        String domain = readable.getSubfolderName();
        String id = readable.getId();

        read(readable, domain, id);
    }

    @Override
    public void readTo(HierarchicalConfig readable, String domain, String id) {
        read(readable, domain, id);
    }

    @Override
    public void readTo(HierarchicalConfig readable, String domain, Identifier id) {
        read(readable, domain, id);
    }

    private void read(Readable readable, String domain, String id) {
        Path filePath = jsonStoragePath.resolveConfigBasedJsonFilePath(domain, id);

        read(readable, filePath);
    }

    private void read(Readable readable, String domain, Identifier id) {
        Path filePath = jsonStoragePath.resolveConfigBasedJsonFilePath(domain, id);

        read(readable, filePath);
    }

    private void read(Readable readable, Path filePath) {
        JsonObject obj = fileReader.readJsonFile(filePath.toFile());
        if (obj == null) {
            obj = new JsonObject();
        }

        StorageReadBuffer reader = new JsonStorageReadBuffer(obj);
        readable.readFrom(reader);
    }
}
