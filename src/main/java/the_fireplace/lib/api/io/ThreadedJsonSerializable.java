package the_fireplace.lib.api.io;

import the_fireplace.lib.api.multithreading.ThreadedSaveHandler;
import the_fireplace.lib.api.multithreading.ThreadedSaveable;

import java.io.File;
import java.nio.file.Path;
import java.util.UUID;

public abstract class ThreadedJsonSerializable implements ThreadedSaveable, JsonReadable, JsonWritable {
    private final ThreadedSaveHandler<ThreadedJsonSerializable> saveHandler = ThreadedSaveHandler.create(this);
    private final File saveFile;
    private final UUID objectId;

    protected ThreadedJsonSerializable(UUID objectId, Path saveFolder) {
        this.objectId = objectId;
        File folder = saveFolder.toFile();
        folder.mkdirs();
        saveFile = new File(folder, FileNames.jsonFileNameFromUUID(objectId));
    }

    @Override
    public void save() {
        if (!isDefaultData()) {
            ThreadedSaveable.super.save();
        } else if(hasSaveFile()) {
            saveFile.delete();
        }
    }

    @Override
    public void blockingSave() {
        writeToJson(saveFile);
    }

    @Override
    public ThreadedSaveHandler<?> getSaveHandler() {
        return saveHandler;
    }

    public boolean loadSavedData() {
        if(hasSaveFile()) {
            load(saveFile);
            return true;
        }
        return false;
    }

    protected boolean hasSaveFile() {
        return saveFile.exists();
    }

    protected void delete() {
        //noinspection ResultOfMethodCallIgnored
        saveFile.delete();
        saveHandler.disposeReferences();
    }

    protected boolean isDefaultData() {
        return false;
    }

    public UUID getId() {
        return objectId;
    }

    public File getSaveFile() {
        return saveFile;
    }
}
