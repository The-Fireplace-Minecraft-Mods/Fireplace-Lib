package dev.the_fireplace.lib.api.storage.utility;

import dev.the_fireplace.lib.impl.storage.utility.SaveTimerImpl;

public interface SaveTimer {
    static SaveTimer getInstance() {
        //noinspection deprecation
        return SaveTimerImpl.INSTANCE;
    }

    void registerSaveFunction(short saveIntervalInMinutes, Runnable... saveRunnables);

    void prepareForServerShutdown();

    void resetTimer();
}
