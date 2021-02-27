package dev.the_fireplace.lib.api.storage.utility;

import dev.the_fireplace.lib.impl.storage.utility.SaveTimerImpl;

public interface SaveTimer {
    static SaveTimer getInstance() {
        //noinspection deprecation
        return SaveTimerImpl.INSTANCE;
    }

    /**
     * @deprecated Use {@link SaveTimer#register} instead.
     */
    @Deprecated
    void registerSaveFunction(short saveIntervalInMinutes, Runnable... saveRunnables);

    void register(short saveIntervalInMinutes, Runnable... saveRunnables);

    void unregister(short saveIntervalInMinutes, Runnable... saveRunnables);

    void prepareForServerShutdown();

    void resetTimer();
}
