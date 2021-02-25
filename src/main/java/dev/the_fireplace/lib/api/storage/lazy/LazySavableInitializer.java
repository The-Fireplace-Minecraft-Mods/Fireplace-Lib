package dev.the_fireplace.lib.api.storage.lazy;

import dev.the_fireplace.lib.api.storage.utility.SaveTimer;

public final class LazySavableInitializer {
    private static final SaveTimer TIMER = SaveTimer.getInstance();

    public static <T extends ThreadsafeLazySavable> T lazyInitialize(T savable, int saveIntervalInMinutes) {
        saveIntervalInMinutes = Math.min(saveIntervalInMinutes, Short.MAX_VALUE);
        savable.load();
        if (saveIntervalInMinutes > 0) {
            TIMER.registerSaveFunction((short) saveIntervalInMinutes, savable::save);
        } else if (isNonDefault(savable)) {
            savable.forceSave();
        }

        return savable;
    }

    private static boolean isNonDefault(Object obj) {
        return !(obj instanceof Defaultable) || !((Defaultable) obj).isDefault();
    }
}
