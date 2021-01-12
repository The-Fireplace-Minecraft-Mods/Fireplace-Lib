package the_fireplace.lib.api.io;

import the_fireplace.lib.impl.io.SaveTimerImpl;

public interface SaveTimer {
    static SaveTimer getInstance() {
        //noinspection deprecation
        return SaveTimerImpl.INSTANCE;
    }

    void registerSaveFunction(short saveIntervalInMinutes, Runnable... saveRunnables);

    void prepareForServerShutdown();

    void resetTimer();
}
