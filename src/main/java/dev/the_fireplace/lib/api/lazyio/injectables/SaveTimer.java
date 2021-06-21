package dev.the_fireplace.lib.api.lazyio.injectables;

public interface SaveTimer {
    void register(short saveIntervalInMinutes, Runnable... saveRunnables);

    void unregister(short saveIntervalInMinutes, Runnable... saveRunnables);

    void prepareForServerShutdown();

    void resetTimer();
}
