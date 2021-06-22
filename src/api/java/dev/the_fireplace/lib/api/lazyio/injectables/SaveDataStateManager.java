package dev.the_fireplace.lib.api.lazyio.injectables;

import dev.the_fireplace.lib.api.lazyio.interfaces.SaveData;

import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public interface SaveDataStateManager {
    void initializeWithAutosave(SaveData saveData, short saveIntervalInMinutes);
    void initializeWithoutAutosave(SaveData saveData);
    void markChanged(SaveData saveData);
    void load(SaveData saveData);
    void save(SaveData saveData);
    /**
     * Force a blocking, non-lazy save.
     */
    void forceSave(SaveData saveData);
    void tearDown(SaveData saveData);
}
