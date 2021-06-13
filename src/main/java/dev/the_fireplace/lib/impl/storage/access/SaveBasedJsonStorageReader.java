package dev.the_fireplace.lib.impl.storage.access;

import com.google.gson.JsonObject;
import dev.the_fireplace.annotateddi.di.Implementation;
import dev.the_fireplace.lib.api.io.JsonFileReader;
import dev.the_fireplace.lib.api.storage.SaveBasedSerializable;
import dev.the_fireplace.lib.api.storage.access.SaveBasedStorageReader;
import dev.the_fireplace.lib.api.storage.access.intermediary.StorageReadBuffer;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Implementation
@Singleton
public final class SaveBasedJsonStorageReader implements SaveBasedStorageReader {
    @SuppressWarnings("HardcodedFileSeparator")
    private static final Pattern JSON_FILE_REGEX = Pattern.compile('^' + SchemaValidator.SCHEMA_PATTERN_STRING + "\\.json$");
    private static final Pattern JSON_EXTENSION_LITERAL = Pattern.compile(".json", Pattern.LITERAL);
    private final JsonFileReader fileReader;

    @Inject
    public SaveBasedJsonStorageReader(JsonFileReader jsonFileReader) {
        fileReader = jsonFileReader;
    }

    @Override
    public void readTo(SaveBasedSerializable readable) {
        Path filePath = JsonStoragePath.resolveSaveBasedJsonFilePath(readable);

        JsonObject obj = fileReader.readJsonFile(filePath.toFile());
        if (obj == null) {
            obj = new JsonObject();
        }

        StorageReadBuffer reader = new JsonStorageReadBuffer(obj);
        readable.readFrom(reader);
    }

    @Override
    public Iterator<String> getStoredIdsIterator(String database, String table) {
        Path saveDirectory = JsonStoragePath.resolveSaveBasedJsonDirectory(database, table);

        File[] files = saveDirectory.toFile().listFiles((file, s) -> JSON_FILE_REGEX.matcher(s).matches());

        return Arrays.stream(files == null ? new File[]{} : files).map(f ->
            JSON_EXTENSION_LITERAL.matcher(f.getName().toLowerCase(Locale.ROOT)).replaceAll(Matcher.quoteReplacement(""))
        ).iterator();
    }

    @Override
    public boolean isStored(String database, String table, String id) {
        return JsonStoragePath.resolveSaveBasedJsonFilePath(database, table, id).toFile().exists();
    }

    @Override
    public boolean isStored(SaveBasedSerializable saveBasedSerializable) {
        return isStored(saveBasedSerializable.getDatabase(), saveBasedSerializable.getTable(), saveBasedSerializable.getId());
    }
}
