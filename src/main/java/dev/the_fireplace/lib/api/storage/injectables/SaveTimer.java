package dev.the_fireplace.lib.api.storage.injectables;

public interface SaveTimer {
    void register(short saveIntervalInMinutes, Runnable... saveRunnables);

    void unregister(short saveIntervalInMinutes, Runnable... saveRunnables);

    void prepareForServerShutdown();

    void resetTimer();
}
