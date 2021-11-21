package dev.the_fireplace.lib.api.lazyio.injectables;

import dev.the_fireplace.lib.api.lazyio.interfaces.SaveData;

import javax.annotation.concurrent.ThreadSafe;

@SuppressWarnings("unused")
@ThreadSafe
public interface SaveDataStateManager
{
    /**
     * Initialize your save data - reads from storage, prepares autosave to lazy save at the specified interval, and marks it as changed for an initial save if not in storage
     */
    void initializeWithAutosave(SaveData saveData, short saveIntervalInMinutes);

    /**
     * Initialize your save data - reads from storage, and performs an initial save if not in storage
     */
    void initializeWithoutAutosave(SaveData saveData);

    /**
     * Mark the data as changed so the next lazy save will write it to the storage
     */
    void markChanged(SaveData saveData);

    /**
     * Manually load the save data. This is only needed if you're reloading data or manually initializing without using {@link #initializeWithAutosave} or {@link #initializeWithoutAutosave}
     */
    void load(SaveData saveData);

    /**
     * Lazy, non-blocking save if changed.
     */
    void save(SaveData saveData);

    /**
     * Force an immediate save that occurs even if not changed.
     */
    void forceSave(SaveData saveData);

    /**
     * Tear down references related to this save data so it can be garbage collected, and remove autosave if applicable. Does not delete the saved data from the storage.
     */
    void tearDown(SaveData saveData);

    /**
     * Tear down references related to this saved data and delete it from the storage.
     */
    void delete(SaveData saveData);
}
