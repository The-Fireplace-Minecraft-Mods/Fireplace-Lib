package the_fireplace.lib.api.io;

import the_fireplace.lib.api.multithreading.ExecutionManager;

import javax.annotation.concurrent.ThreadSafe;
import java.io.File;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@ThreadSafe
public abstract class JsonSerializable implements JsonReadable, JsonWritable {
    private final File saveFile;
    private final UUID objectId;
    private final AtomicBoolean isChanged = new AtomicBoolean(false);
    private final AtomicBoolean saving = new AtomicBoolean(false);

    protected JsonSerializable(UUID objectId, Path saveFolder) {
        this.objectId = objectId;
        File folder = saveFolder.toFile();
        folder.mkdirs();
        saveFile = new File(folder, FileNameResolver.getInstance().jsonFileNameFromUUID(objectId));
    }

    public void markChanged() {
        isChanged.lazySet(true);
    }

    public void save() {
        if (!isDefaultData()) {
            if (canSave()) {
                startSave();
            }
        } else if(hasSaveFile()) {
            deleteSaveFile();
        }
    }

    private boolean canSave() {
        return isChanged.get() && !saving.get();
    }

    private synchronized void startSave() {
        saving.set(true);
        isChanged.set(false);
        ExecutionManager.getInstance().run(() -> {
            writeToJson(saveFile);
            saving.set(false);
        });
    }

    public boolean loadSavedData() {
        if (hasSaveFile()) {
            load(saveFile);
            return true;
        }
        return false;
    }

    protected boolean hasSaveFile() {
        return saveFile.exists();
    }

    protected void deleteSaveFile() {
        //noinspection ResultOfMethodCallIgnored
        saveFile.delete();
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
