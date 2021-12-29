package dev.the_fireplace.lib.io.access;

import com.google.gson.JsonObject;
import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.io.injectables.JsonFileReader;
import dev.the_fireplace.lib.api.io.injectables.SaveBasedStorageReader;
import dev.the_fireplace.lib.api.io.interfaces.SaveBasedSerializable;
import dev.the_fireplace.lib.api.io.interfaces.access.StorageReadBuffer;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.Matcher;

@Implementation
@Singleton
public final class SaveBasedJsonStorageReader implements SaveBasedStorageReader {
    private final JsonFileReader fileReader;
    private final JsonStoragePath jsonStoragePath;

    @Inject
    public SaveBasedJsonStorageReader(JsonFileReader jsonFileReader, JsonStoragePath jsonStoragePath) {
        this.fileReader = jsonFileReader;
        this.jsonStoragePath = jsonStoragePath;
    }

    @Override
    public void readTo(SaveBasedSerializable readable) {
        Path filePath = jsonStoragePath.resolveSaveBasedJsonFilePath(readable);

        JsonObject obj = fileReader.readJsonFile(filePath.toFile());
        if (obj == null) {
            obj = new JsonObject();
        }

        StorageReadBuffer reader = new JsonStorageReadBuffer(obj);
        readable.readFrom(reader);
    }

    @Override
    public Iterator<String> getStoredIdsIterator(String database, String table) {
        Path saveDirectory = jsonStoragePath.resolveSaveBasedJsonDirectory(database, table);

        File[] files = saveDirectory.toFile().listFiles((file, s) -> JsonFileConstants.JSON_FILE_REGEX.matcher(s).matches());

        return Arrays.stream(files == null ? new File[]{} : files).map(f ->
            JsonFileConstants.JSON_EXTENSION_LITERAL.matcher(f.getName().toLowerCase(Locale.ROOT)).replaceAll(Matcher.quoteReplacement(""))
        ).iterator();
    }

    @Override
    public boolean isStored(String database, String table, String id) {
        return jsonStoragePath.resolveSaveBasedJsonFilePath(database, table, id).toFile().exists();
    }

    @Override
    public boolean isStored(SaveBasedSerializable saveBasedSerializable) {
        return isStored(saveBasedSerializable.getDatabase(), saveBasedSerializable.getTable(), saveBasedSerializable.getId());
    }
}
