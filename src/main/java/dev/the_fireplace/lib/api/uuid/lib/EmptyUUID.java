package dev.the_fireplace.lib.api.uuid.lib;

import java.util.UUID;

public final class EmptyUUID {
    public static final UUID EMPTY_UUID = new UUID(0, 0);

    public static boolean isEmpty(UUID uuid) {
        return EMPTY_UUID.equals(uuid);
    }
}
