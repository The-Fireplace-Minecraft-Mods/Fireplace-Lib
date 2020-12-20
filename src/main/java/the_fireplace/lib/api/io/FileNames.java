package the_fireplace.lib.api.io;

import java.util.UUID;

public final class FileNames {
    public static String jsonFileNameFromUUID(UUID uuid) {
        return uuid.toString()+".json";
    }
}
