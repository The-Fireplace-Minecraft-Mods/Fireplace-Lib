package dev.the_fireplace.lib.api.storage.lazy;

import dev.the_fireplace.lib.api.storage.utility.SaveTimer;

public final class LazySavableInitializer {
    private static final SaveTimer TIMER = SaveTimer.getInstance();

    public static <T extends ThreadsafeLazySavable> T lazyInitialize(T savable, int saveIntervalInMinutes) {
        savable.load();
        if (saveIntervalInMinutes > 0) {
            TIMER.register(boundToShort(saveIntervalInMinutes), savable::save);
        } else if (isNonDefault(savable)) {
            savable.forceSave();
        }

        return savable;
    }

    public static void tearDown(ThreadsafeLazySavable savable, int saveIntervalInMinutes) {
        TIMER.unregister(boundToShort(saveIntervalInMinutes), savable::save);
    }

    private static short boundToShort(int saveIntervalInMinutes) {
        return (short)Math.max(Short.MIN_VALUE, Math.min(Short.MAX_VALUE, saveIntervalInMinutes));
    }

    private static boolean isNonDefault(Object obj) {
        return !(obj instanceof Defaultable) || !((Defaultable) obj).isDefault();
    }
}
