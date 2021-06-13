package dev.the_fireplace.lib.api.storage.utility;

public interface SaveTimer {
    void register(short saveIntervalInMinutes, Runnable... saveRunnables);

    void unregister(short saveIntervalInMinutes, Runnable... saveRunnables);

    void prepareForServerShutdown();

    void resetTimer();
}
